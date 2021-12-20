package com.example.chat.domain;

import com.example.chat.model.MessageRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Message {

    private String id;
    private int num;
    private Type type;
    private String groupId;
    private User sender;
    private String createdAt;
    private String text;
    private String detectedLanguage;
    private Set<MessageWithLanguage> translatedMessage;
    @JsonIgnore
    private String disruptiveScore;

    @Builder
    public Message(Type type, String groupId, User sender) {
        this.type = type;
        this.groupId = groupId;
        this.sender = sender;
    }

    public enum Type {
        ENTER, TALK, EXIT
    }

    public static Message newMessageOf(MessageRequest messageRequest, int messageNum) {
        Message message = new Message();

        message.setId(UUID.randomUUID().toString());
        message.setNum(messageNum);
        message.setCreatedAt(java.time.Instant.now().toString());

        message.setType(messageRequest.getType());
        message.setGroupId(messageRequest.getGroupId());
        message.setSender(messageRequest.getSender());
        message.setText(messageRequest.getText());
        return message;
    }

}
