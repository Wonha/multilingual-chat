package com.example.chat.repository;

import com.example.chat.domain.ChatGroup;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GroupRepository {

    private Map<String, ChatGroup> id2Group = new HashMap<>();

    public List<ChatGroup> findAll() {
        return new ArrayList<>(this.id2Group.values());
    }

    public ChatGroup findById(String id) {
        return this.id2Group.get(id);
    }

    public ChatGroup save(ChatGroup group) {
        this.id2Group.put(group.getId(), group);
        return group;
    }

    public void removeById(String id) {
        this.id2Group.remove(id);
    }
}
