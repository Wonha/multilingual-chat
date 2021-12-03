package com.example.chat.repository;

import com.example.chat.model.ChatGroup;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GroupRepository {
    private Map<String, ChatGroup> groupId2Group = new HashMap<>();

    public List<ChatGroup> findAll() {
        return new ArrayList<>(groupId2Group.values());
    }

    public ChatGroup findGroupById(String id) {
        return groupId2Group.get(id);
    }

    public ChatGroup save(ChatGroup group) {
        groupId2Group.put(group.getId(), group);
        return group;
    }

    public void remove(String id) {
        groupId2Group.remove(id);
    }
}
