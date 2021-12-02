package com.example.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class Message {

    private String id;
    public enum Type {
        ENTER, TALK, EXIT
    }
    private Type type;
    private String groupId;
    private User sender;
    private String createdAt;
    private String text;
    private Set<TranslatedMessage> translatedMessage;
    @JsonIgnore
    private String disruptiveScore;

    @Builder
    public Message(Type type, String groupId, User sender) {
        this.type = type;
        this.groupId = groupId;
        this.sender = sender;
    }
}
