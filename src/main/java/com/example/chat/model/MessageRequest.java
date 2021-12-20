package com.example.chat.model;

import com.example.chat.domain.Message;
import com.example.chat.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageRequest {

    private Message.Type type;
    private String groupId;
    private User sender;
    private String text;

    @Builder
    public MessageRequest(Message.Type type, String groupId, User sender) {
        this.type = type;
        this.groupId = groupId;
        this.sender = sender;
    }
}
