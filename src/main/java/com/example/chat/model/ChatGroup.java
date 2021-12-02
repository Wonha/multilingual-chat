package com.example.chat.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
