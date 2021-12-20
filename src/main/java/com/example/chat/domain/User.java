package com.example.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;
    private String name;
    private String photoUrl;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String lang;
    private Set<String> groups;

    public static User newSystemUser() {
        User user = new User();
        user.setName("System");
        user.setLang("en");
        return user;
    }
}
