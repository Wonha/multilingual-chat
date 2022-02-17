/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.example.chat.service;

import com.example.chat.client.PerspectiveClient;
import com.example.chat.client.TranslationClient;
import com.example.chat.config.ServerConfig;
import com.example.chat.domain.ChatGroup;
import com.example.chat.domain.Message;
import com.example.chat.domain.Translation;
import com.example.chat.domain.User;
import com.example.chat.model.MessageDisplay;
import com.example.chat.model.MessageRequest;
import com.example.chat.repository.MessageRepository;
import com.example.chat.repository.SessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {

    private final TranslationClient translationClient;
    private final PerspectiveClient perspectiveClient;
    private final GroupService groupService;
    private final ServerConfig serverConfig;
    private final MessageRepository messageRepository;
    private final SessionRepository sessionRepository;

    private final ObjectMapper objectMapper;

    public void handleMessage(WebSocketSession session, MessageRequest messageRequest) {
        ChatGroup group = this.groupService.findById(messageRequest.getGroupId());
        if (null == group) {
            sendMessage(session, "Chat group not exist " + messageRequest.getGroupId());
            return;
        }

        Message message = processMessageType(messageRequest, session, group);

        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        Translation original = detectOriginalMessageAntidote(message.getText(), completableFutures);
        Map<String, Translation> lang2Translations = detectTranslationsAntidote(message.getText(),
                group.findPreferredLanguages(), completableFutures);
        completableFutures.forEach(CompletableFuture::join);
        lang2Translations.put(original.getLang(), original);
        log.debug("Completion <<");

        message.setTranslatedMessage(new HashSet<>(lang2Translations.values()));
        this.messageRepository.save(message);

        // Send message to users in group
        MessageDisplay latestMessageDisplay = MessageDisplay.of(message, original);
        Set<MessageDisplay> display = new HashSet<>();
        display.add(latestMessageDisplay);
        group.getUsers().forEach(u -> {
            Set<Translation> userTranslations = new HashSet<>();
            userTranslations.add(lang2Translations.get(u.getLang()));
            latestMessageDisplay.setTranslations(userTranslations);
            sendMessage(sessionRepository.findByGroupUser(group.getId(), u.getName()).getSession(), display);
        });
    }

    private Map<String, Translation> detectTranslationsAntidote(
            String text, Set<String> targetLanguages, List<CompletableFuture<Void>> cList) {

        return targetLanguages.stream()
                .collect(Collectors.toMap(Function.identity(), (lang -> {
                    Translation translated = Translation.builder().lang(lang).build();
                    CompletableFuture<Void> future = this.translationClient
                            .translate(text, translated.getLang())
                            .thenApply(t -> {
                                translated.setText(t);
                                return this.perspectiveClient.perspective(t, lang);
                            })
                            .thenAccept(p -> translated.detoxifyText(p.getSummaryScore(),
                                    serverConfig.toxicityThreshold));
                    cList.add(future);
                    return translated;
                })));
    }

    private Translation detectOriginalMessageAntidote(
            String text, List<CompletableFuture<Void>> completableFutures) {

        Translation original = Translation.builder()
                .text(text)
                .isOriginal(true)
                .build();
        CompletableFuture<Void> future = this.translationClient.detectLanguage(text)
                .thenApply(l -> {
                    original.setLang(l);
                    return this.perspectiveClient.perspective(original.getText(), l);
                })
                .thenAccept(p -> original.detoxifyText(p.getSummaryScore(),
                        serverConfig.toxicityThreshold));
        completableFutures.add(future);
        return original;
    }

    public ChatGroup deleteGroupById(String groupId) {
        this.sessionRepository.removeByGroupId(groupId);
        return this.groupService.deleteGroupById(groupId);
    }

    public MessageRequest newExitMessageOf(String sessionId) {
        ChatGroup group = this.groupService.findById(
                this.sessionRepository.findGroupIdBySessionId(sessionId));
        String userName = this.sessionRepository.findUserNameBySessionId(sessionId);

        User sender = null;
        for (User u : group.getUsers()) {
            if (u.getName().equals(userName)) {
                sender = u;
            }
        }

        return MessageRequest.builder()
                .type(Message.Type.EXIT)
                .groupId(group.getId())
                .sender(sender)
                .build();
    }

    private <T> void sendMessage(WebSocketSession session, T message) {
        try {
            log.debug("Broadcast {}", message);
            session.sendMessage(new TextMessage(this.objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private Message processMessageType(MessageRequest messageRequest, WebSocketSession session, ChatGroup group) {
        Message message = Message.of(messageRequest, group.increaseAndGetMessageNum());
        switch (message.getType()) {
            case ENTER:
                this.sessionRepository.save(session, group.getId(), message.getSender().getName());
                this.groupService.enterGroup(group, message.getSender());
                overrideSystemMessage(message, " has joined");
                break;
            case TALK:
                break;
            case EXIT:
                this.sessionRepository.removeById(session.getId());
                this.groupService.exitGroup(group, message.getSender());
                overrideSystemMessage(message, " has left");
                break;
            default:
        }
        return message;
    }

    private void overrideSystemMessage(Message message, String text) {
        message.setText(message.getSender().getName() + text);
        message.setSender(User.newSystemUser());
    }

}
