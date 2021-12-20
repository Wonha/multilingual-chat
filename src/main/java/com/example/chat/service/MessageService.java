package com.example.chat.service;

import com.example.chat.client.TranslationClient;
import com.example.chat.domain.*;
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
    private final GroupService groupService;
    private final MessageRepository messageRepository;
    private final SessionRepository sessionRepository;

    private final ObjectMapper objectMapper;

    public MessageRequest newExitMessageOf(String sessionId) {
        ChatGroup group = this.groupService.findById(
                this.sessionRepository.findGroupIdBySessionId(sessionId));
        String userId = this.sessionRepository.findUserIdBySessionId(sessionId);

        User sender = null;
        for (User u : group.getUsers()) {
            if (u.getId().equals(userId)) {
                sender = u;
            }
        }

        return MessageRequest.builder()
                .type(Message.Type.EXIT)
                .groupId(group.getId())
                .sender(sender)
                .build();
    }

    public void handleMessage(WebSocketSession session, MessageRequest messageRequest) {
        ChatGroup group = this.groupService.findById(messageRequest.getGroupId());
        if (null == group) {
            sendMessage(session, "Chat group not exist " + messageRequest.getGroupId());
            return;
        }

        Message message = processMessageType(messageRequest, session, group);

        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();

        // Detect original language
        MessageWithLanguage original = MessageWithLanguage.builder()
                .text(message.getText())
                .isOriginal(true)
                .build();
        CompletableFuture<Void> detection = this.translationClient.detectLanguage(message.getText())
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
        this.messageRepository.save(message);

        // Send translations to users in group
        group.getUsers().forEach(u -> {
            Set<MessageWithLanguage> translation = new HashSet<>();
            if (!Objects.equals(original.getText(), u.getLang())) {
                MessageWithLanguage preferredTranslation = lang2Translation.get(u.getLang());
                translation.add(preferredTranslation);
            }

            Set<MessageDisplay> display = new HashSet<>();
            MessageDisplay latestMessage = MessageDisplay.builder()
                    .id(message.getId())
                    .num(message.getNum())
                    .lang(original.getLang())
                    .text(original.getText())
                    .type(message.getType())
                    .sender(message.getSender())
                    .createdAt(message.getCreatedAt())
                    .translations(translation)
                    .build();
            display.add(latestMessage);
            sendMessage(sessionRepository.findByGroupUser(group.getId(), u.getId()).getSession(), display);
        });
    }

    private Message processMessageType(MessageRequest messageRequest, WebSocketSession session, ChatGroup group) {
        Message message = Message.newMessageOf(messageRequest, group.increaseAndGetMessageNum());

        switch (message.getType()) {
            case ENTER:
                this.sessionRepository.save(session, group.getId(), message.getSender().getId());
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

    private Map<String, MessageWithLanguage> createTranslations(String text, Set<String> targetLanguages, List<CompletableFuture<Void>> cList) {
        return targetLanguages.stream()
                .collect(Collectors.toMap(Function.identity(), (lang -> {
                    MessageWithLanguage translated = MessageWithLanguage.builder().lang(lang).build();
                    CompletableFuture<Void> c = this.translationClient
                            .translate(text, translated.getLang())
                            .thenAccept(translated::setText);
                    cList.add(c);
                    return translated;
                })));
    }

    private <T> void sendMessage(WebSocketSession session, T message) {
        try {
            log.debug("Response {}", message);
            session.sendMessage(new TextMessage(this.objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public ChatGroup deleteGroupById(String groupId) {
        this.sessionRepository.removeByGroupId(groupId);
        return this.groupService.deleteGroupById(groupId);
    }
}
