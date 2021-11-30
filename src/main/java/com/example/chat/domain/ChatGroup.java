package com.example.chat.domain;

import com.example.chat.service.ChatService;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ChatGroup {
    private String id;
    private String name;
    private Set<User> users = new HashSet<>();

    @Builder
    public ChatGroup(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
