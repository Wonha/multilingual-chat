package com.example.chat.domain;

import com.example.chat.config.ServerConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
        user.setName(ServerConfig.SYSTEM_NAME);
        user.setLang(ServerConfig.SYSTEM_LANG);
        return user;
    }
}
