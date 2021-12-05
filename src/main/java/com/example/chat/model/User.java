package com.example.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
@EqualsAndHashCode(of = "webSocketSession")
public class User {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String photoUrl;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String lang;

    @JsonIgnore
    private WebSocketSession webSocketSession;

    public static User newSystemUser(String lang) {
        User user = new User();
        user.setName("System");
        user.setLang(lang);
        return user;
    }
}
