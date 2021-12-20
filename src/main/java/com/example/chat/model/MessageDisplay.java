package com.example.chat.model;

import com.example.chat.domain.Message;
import com.example.chat.domain.MessageWithLanguage;
import com.example.chat.domain.User;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
public class MessageDisplay {
    private String id;
    private int num;
    private String lang;
    private String text;
    private Message.Type type;
    private String createdAt;
    private User sender;
    private Set<MessageWithLanguage> translations;

    @Builder
    public MessageDisplay(
            String id,
            int num,
            String lang,
            String text,
            Message.Type type,
            String createdAt,
            User sender,
            Set<MessageWithLanguage> translations) {
        this.id = id;
        this.num = num;
        this.lang = lang;
        this.text = text;
        this.type = type;
        this.createdAt = createdAt;
        this.sender = sender;
        this.translations = translations;
    }
}
