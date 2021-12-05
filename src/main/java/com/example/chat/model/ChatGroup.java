package com.example.chat.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ChatGroup {
    private String id;
    private String name;
    private int messageNum;
    private Set<User> users = new HashSet<>();

    @Builder
    public ChatGroup(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Set<String> findPreferredLanguages() {
        return this.getUsers().stream()
                .map(User::getLang)
                .collect(Collectors.toSet());
    }

    public boolean isEmpty() {
        return this.getUsers().size() <= 0;
    }

    public int increaseAndGetMessageNum() {
        return this.messageNum++;
    }
}
