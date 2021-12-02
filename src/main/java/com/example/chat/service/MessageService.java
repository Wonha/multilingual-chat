package com.example.chat.service;

import com.example.chat.client.TranslationClient;
import com.example.chat.model.ChatGroup;
import com.example.chat.model.Message;
import com.example.chat.model.TranslatedMessage;
import com.example.chat.model.User;
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
    private final ChatGroupService chatGroupService;

    public void handleMessage(WebSocketSession session, Message message) {
        ChatGroup group = chatGroupService.findGroupById(message.getGroupId());
        if (null == group) {
            sendMessage(session, "Chat group not exist " + message.getGroupId());
            return;
        }

        processMessageType(message, group);

        // Create translations in target language
        Map<String, TranslatedMessage> lang2Translation = createTranslations(message.getText(),
                group.getUsers().stream()
                        .map(User::getLang)
                        .collect(Collectors.toSet()));
        message.setTranslatedMessage(new HashSet<>(lang2Translation.values()));

        // Send translations to users in group
        group.getUsers().forEach(u -> {
            TranslatedMessage translation = lang2Translation.get(u.getLang());
            sendMessage(u.getWebSocketSession(), translation);
        });
    }

    private void processMessageType(Message message, ChatGroup group) {
        switch (message.getType()) {
            case ENTER:
                group.getUsers().add(message.getSender());
                message.setText("[System] " + message.getSender().getName() + " has entered");
                break;
            case TALK:
                message.setText("[" + message.getSender().getName()+ "] "+ message.getText());
                break;
            case EXIT:
                break;
            default:
        }
    }

    private Map<String, TranslatedMessage> createTranslations(String text, Set<String> targetLanguages) {
        List<CompletableFuture<Void>> cList = new ArrayList<>();

        Map<String, TranslatedMessage> lang2Translation = targetLanguages.stream()
                .collect(Collectors.toMap(Function.identity(), (lang -> {
                    TranslatedMessage translated = new TranslatedMessage();
                    translated.setLang(lang);
                    CompletableFuture<Void> c = translationClient
                            .translate(text, translated.getLang())
                            .thenAccept(translated::setText);
                    cList.add(c);
                    return translated;
                })));

        cList.forEach(CompletableFuture::join);

        return lang2Translation;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
