package com.example.chat.service;

import com.example.chat.model.ChatGroup;
import com.example.chat.model.User;
import com.example.chat.repository.GroupRepository;
import com.example.chat.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final SessionRepository sessionRepository;

    public List<ChatGroup> findAllGroups() {
        return this.groupRepository.findAll();
    }

    public ChatGroup findGroupById(String id) {
        return this.groupRepository.findGroupById(id);
    }

    public ChatGroup createGroup(String name) {
        String newId = UUID.randomUUID().toString();
        ChatGroup newGroup = ChatGroup.builder()
                .id(newId)
                .name(name)
                .build();
        this.groupRepository.save(newGroup);
        return newGroup;
    }

    public void enterGroup(ChatGroup group, User sender) {
        group.getUsers().add(sender);
        this.groupRepository.save(group);
        this.sessionRepository.save(group.getId(), sender.getWebSocketSession());
    }

    public ChatGroup findGroupBySessionId(String sessionId) {
        return this.groupRepository.findGroupById(this.sessionRepository.findGroupBySessionId(sessionId));
    }

    public void exitGroup(ChatGroup group, User sender) {
        group.getUsers().remove(sender);
        if (group.hasNoUser()) {
            this.groupRepository.removeById(group.getId());
        }
        this.sessionRepository.remove(sender.getWebSocketSession().getId());
    }

    public ChatGroup deleteGroupById(String groupId) {
        ChatGroup group = this.groupRepository.findGroupById(groupId);
        if (null == group) {
            return null;
        }

        group.getUsers().forEach(u ->
                this.sessionRepository.remove(u.getWebSocketSession().getId()));
        this.groupRepository.removeById(groupId);
        return group;
    }
}
