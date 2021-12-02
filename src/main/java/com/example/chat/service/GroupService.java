package com.example.chat.service;

import com.example.chat.model.ChatGroup;
import com.example.chat.model.User;
import com.example.chat.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public List<ChatGroup> findAllGroups() {
        return groupRepository.findAllGroups();
    }

    public ChatGroup findGroupById(String id) {
        return groupRepository.findGroupById(id);
    }

    public ChatGroup createGroup(String name) {
        String newId = UUID.randomUUID().toString();
        ChatGroup newGroup = ChatGroup.builder().id(newId).name(name).build();
        groupRepository.createGroup(newGroup);
        return newGroup;
    }

    public void enterGroup(ChatGroup group, User sender) {
        group.getUsers().add(sender);
        groupRepository.addSession(group.getId(), sender.getWebSocketSession());
    }

    public ChatGroup findGroupBySessionId(String sessionId) {
        return groupRepository.findGroupBySessionId(sessionId);
    }

    public void exitGroup(ChatGroup group, User sender) {
        group.getUsers().remove(sender);
        if (group.getUsers().size() <= 0) {
            groupRepository.removeGroup(group.getId());
        }
        groupRepository.removeSession(sender.getWebSocketSession().getId());
    }
}
