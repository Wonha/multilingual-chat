package com.example.chat.model;

import com.example.chat.domain.Message;
import com.example.chat.domain.Translation;
import com.example.chat.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private Set<Translation> translations;

    @Builder
    public MessageDisplay(
            String id,
            int num,
            String lang,
            String text,
            Message.Type type,
            String createdAt,
            User sender,
            Set<Translation> translations) {
        this.id = id;
        this.num = num;
        this.lang = lang;
        this.text = text;
        this.type = type;
        this.createdAt = createdAt;
        this.sender = sender;
        this.translations = translations;
    }

    public static MessageDisplay of(Message message, Translation original) {
        return MessageDisplay.builder()
                .id(message.getId())
                .num(message.getNum())
                .lang(original.getLang())
                .text(original.getText())
                .type(message.getType())
                .sender(message.getSender())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
