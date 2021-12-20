package com.example.chat.repository;

import com.example.chat.domain.Message;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MessageRepository {
    private Map<String, Message> id2Message = new HashMap<>();

    public String save(Message message) {
        this.id2Message.put(message.getId(), message);
        return message.getId();
    }
}
