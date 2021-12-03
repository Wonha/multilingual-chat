package com.example.chat.service;

import com.example.chat.client.TranslationClient;
import com.example.chat.model.ChatGroup;
import com.example.chat.model.Message;
import com.example.chat.model.MessageWithLanguage;
import com.example.chat.model.User;
import com.example.chat.repository.MessageRepository;
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
    private final ObjectMapper objectMapper;
    private final TranslationClient translationClient;
    private final GroupService groupService;
    private final MessageRepository messageRepository;

    public void handleMessage(WebSocketSession session, Message message) {
        ChatGroup group = groupService.findGroupById(message.getGroupId());
        if (null == group) {
            sendMessage(session, "Chat group not exist " + message.getGroupId());
            return;
        }

        processMessageType(message, group);

        // Detect original language
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        MessageWithLanguage original = MessageWithLanguage.builder().text(message.getText()).build();
        original.setOriginal(true);
        CompletableFuture<Void> detection = translationClient.detectLanguage(message.getText())
                .thenAccept(original::setLang);
        completableFutures.add(detection);

        // Create translations in target language
        Map<String, MessageWithLanguage> lang2Translation = createTranslations(message.getText(),
                group.findPreferredLanguages(),
                completableFutures);
        completableFutures.forEach(CompletableFuture::join);
        log.debug("Completion <<");

        lang2Translation.put(original.getLang(), original);
        message.setTranslatedMessage(new HashSet<>(lang2Translation.values()));
        messageRepository.save(message);

        // Send translations to users in group
        group.getUsers().forEach(u -> {
            MessageWithLanguage translation = lang2Translation.get(u.getLang());
            Set<MessageWithLanguage> messages = new HashSet<>(Arrays.asList(original, translation));
            sendMessage(u.getWebSocketSession(), messages);
        });
    }

    private void processMessageType(Message message, ChatGroup group) {
        message.setCreatedAt(java.time.Instant.now().toString());
        message.setId(UUID.randomUUID().toString());
        switch (message.getType()) {
            case ENTER:
                groupService.enterGroup(group, message.getSender());
                message.setText("[System] " + message.getSender().getName() + " has joined");
                break;
            case TALK:
                message.setText("[" + message.getSender().getName()+ "] "+ message.getText());
                break;
            case EXIT:
                groupService.exitGroup(group, message.getSender());
                message.setText("[System] " + message.getSender().getName() + " has left");
                break;
            default:
        }
    }

    private Map<String, MessageWithLanguage> createTranslations(String text, Set<String> targetLanguages, List<CompletableFuture<Void>> cList) {
        return targetLanguages.stream()
                .collect(Collectors.toMap(Function.identity(), (lang -> {
                    MessageWithLanguage translated = MessageWithLanguage.builder().lang(lang).build();
                    CompletableFuture<Void> c = translationClient
                            .translate(text, translated.getLang())
                            .thenAccept(translated::setText);
                    cList.add(c);
                    return translated;
                })));
    }

    public Message newExitMessageOf(String sessionId) {
        ChatGroup group = groupService.findGroupBySessionId(sessionId);
        User sender = null;
        for (User u : group.getUsers()) {
            if (u.getWebSocketSession().getId().equals(sessionId)) {
                sender = u;
            }
        }
        return Message.builder()
                .type(Message.Type.EXIT)
                .groupId(group.getId())
                .sender(sender)
                .build();
    }

    private <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
