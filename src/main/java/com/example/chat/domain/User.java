package com.example.chat.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
@EqualsAndHashCode(of = "webSocketSession")
public class User {
    private String id;
    private String name;
    private String lang;
    private WebSocketSession webSocketSession;
}
