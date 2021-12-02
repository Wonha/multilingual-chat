package com.example.chat.service;

import com.example.chat.model.ChatGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatGroupService {
    private Map<String, ChatGroup> groupId2Group = new HashMap<>(); // todo Move to distributed cache

    public List<ChatGroup> findAllGroups() {
        return new ArrayList<>(groupId2Group.values());
    }

    public ChatGroup findGroupById(String id) {
        return groupId2Group.get(id);
    }

    public ChatGroup createGroup(String name) {
        String newId = UUID.randomUUID().toString();
        ChatGroup newGroup = ChatGroup.builder().id(newId).name(name).build();
        groupId2Group.put(newId, newGroup);
        return newGroup;
    }

}
