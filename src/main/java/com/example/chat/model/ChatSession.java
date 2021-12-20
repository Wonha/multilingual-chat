package com.example.chat.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
public class ChatSession {

    private String userId;
    private String groupId;
    private WebSocketSession session;

    @Builder
    public ChatSession(String userId, String groupId, WebSocketSession session) {
        this.userId = userId;
        this.groupId = groupId;
        this.session = session;
    }
}
