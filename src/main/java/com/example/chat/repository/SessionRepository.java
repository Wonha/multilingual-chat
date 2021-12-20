package com.example.chat.repository;

import com.example.chat.model.ChatSession;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class SessionRepository {
    private Map<String, ChatSession> sessionId2ChatSession = new HashMap<>();
    private Map<String, String> groupUserId2SessionId = new HashMap<>();

    public void save(WebSocketSession session, String groupId, String userId) {
        this.groupUserId2SessionId.put(toGroupUserId(groupId, userId), session.getId());
        this.sessionId2ChatSession.put(
                session.getId(),
                ChatSession.builder()
                        .userId(userId)
                        .groupId(groupId)
                        .session(session)
                        .build());
    }

    public void removeById(String sessionId) {
        ChatSession chatSession = findById(sessionId);
        this.groupUserId2SessionId.remove(toGroupUserId(chatSession.getGroupId(), chatSession.getUserId()));
        this.sessionId2ChatSession.remove(sessionId);
    }

    public ChatSession findById(String sessionId) {
        return this.sessionId2ChatSession.getOrDefault(sessionId, null);
    }

    public String findGroupIdBySessionId(String sessionId) {
        return this.sessionId2ChatSession.getOrDefault(sessionId, null)
                .getGroupId();
    }

    public String findUserIdBySessionId(String sessionId) {
        return this.sessionId2ChatSession.getOrDefault(sessionId, null)
                .getUserId();
    }

    public void removeByGroupId(String groupId) {
        Set<String> sessions = this.sessionId2ChatSession.entrySet().stream()
                .filter(e -> e.getValue().getGroupId().equals(groupId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        sessions.forEach(this.sessionId2ChatSession::remove);

        Set<String> groupUsers = groupUserId2SessionId.entrySet().stream()
                .filter(e -> sessions.contains(e.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        groupUsers.forEach(this.groupUserId2SessionId::remove);
    }

    public ChatSession findByGroupUser(String groupId, String userId) {
        return findByGroupUserId(toGroupUserId(groupId, userId));
    }

    private ChatSession findByGroupUserId(String groupUserId) {
        return findById(this.groupUserId2SessionId.get(groupUserId));
    }

    private String toGroupUserId(String groupId, String userId) {
        return groupId + userId;
    }

}
