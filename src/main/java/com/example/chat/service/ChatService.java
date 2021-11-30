package com.example.chat.service;

import com.example.chat.client.TranslationClient;
import com.example.chat.domain.ChatGroup;
import com.example.chat.domain.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper;
    private final TranslationClient translationClient;
    private Map<String, ChatGroup> groupId2Group = new HashMap<>(); // todo Move to distributed cache

    public List<ChatGroup> findAllGroups() {
        return new ArrayList<>(groupId2Group.values());
    }

    public ChatGroup findGroupById(String id) {
        return groupId2Group.get(id);
    }

    public ChatGroup createGroup(String name) {
        String newId = UUID.randomUUID().toString();
        ChatGroup newGroup = ChatGroup.builder().id(newId).name(name).build();
        groupId2Group.put(newId, newGroup);
        return newGroup;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void handleMessage(WebSocketSession session, Message message) {
        ChatGroup group = findGroupById(message.getGroupId());
        if (null == group) {
            sendMessage(session, "Chat group not exist " + message.getGroupId());
            return;
        }

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

        Map<String, String> lang2Translation = new HashMap<>();
        group.getUsers().forEach(u -> {
            String translation;
            if (lang2Translation.containsKey(u.getLang())) {
                translation = lang2Translation.get(u.getLang());
            } else {
                translation = translationClient.translate(message.getText(), u.getLang());
                lang2Translation.put(u.getLang(), translation);
            }
            sendMessage(u.getWebSocketSession(), translation);
        });
    }
}
