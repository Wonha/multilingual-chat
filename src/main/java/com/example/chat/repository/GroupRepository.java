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
    private Map<String, String> session2GroupId = new HashMap<>();
    private Map<String, ChatGroup> groupId2Group = new HashMap<>();

    public List<ChatGroup> findAllGroups() {
        return new ArrayList<>(groupId2Group.values());
    }

    public ChatGroup findGroupById(String id) {
        return groupId2Group.get(id);
    }

    public ChatGroup createGroup(ChatGroup newGroup) {
        groupId2Group.put(newGroup.getId(), newGroup);
        return newGroup;
    }

    public ChatGroup findGroupBySessionId(String id) {
        return groupId2Group.get(session2GroupId.get(id));
    }

    public void addSession(String groupId, WebSocketSession webSocketSession) {
        session2GroupId.put(webSocketSession.getId(), groupId);
    }

    public void removeSession(String sessionId) {
        session2GroupId.remove(sessionId);
    }

    public void removeGroup(String id) {
        groupId2Group.remove(id);
    }
}
