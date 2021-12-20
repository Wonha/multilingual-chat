package com.example.chat.domain;

import com.example.chat.model.MessageRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@Builder
public class Message {

    private String id;
    private int num;
    private Type type;
    private String groupId;
    private User sender;
    private String createdAt;
    private String text;
    private String detectedLanguage;
    private Set<Translation> translatedMessage;
    @JsonIgnore
    private String disruptiveScore;

    public enum Type {
        ENTER, TALK, EXIT
    }

    public static Message of(MessageRequest messageRequest, int messageNum) {
        return Message.builder()
                .id(UUID.randomUUID().toString())
                .num(messageNum)
                .type(messageRequest.getType())
                .groupId(messageRequest.getGroupId())
                .sender(messageRequest.getSender())
                .createdAt(Instant.now().toString())
                .text(messageRequest.getText())
                .build();
    }

}
