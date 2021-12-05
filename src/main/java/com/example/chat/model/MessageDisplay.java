package com.example.chat.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class MessageDisplay {
    private String id;
    private MessageWithLanguage original;
    private String lang;
    private String text;
    private Message.Type type;
    private String createdAt;
    private User sender;
    private Set<MessageWithLanguage> translation;

    @Builder
    public MessageDisplay(
            String id,
            String lang,
            String text,
            Message.Type type,
            String createdAt,
            User sender,
            Set<MessageWithLanguage> translation) {
        this.id = id;
        this.lang = lang;
        this.text = text;
        this.type = type;
        this.createdAt = createdAt;
        this.sender = sender;
        this.translation = translation;
    }
}
