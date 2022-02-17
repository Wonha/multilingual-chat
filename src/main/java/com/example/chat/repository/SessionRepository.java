/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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

    public void save(WebSocketSession session, String groupId, String userName) {
        this.groupUserId2SessionId.put(toGroupUserId(groupId, userName), session.getId());
        this.sessionId2ChatSession.put(
                session.getId(),
                ChatSession.builder()
                        .userName(userName)
                        .groupId(groupId)
                        .session(session)
                        .build());
    }

    public void removeById(String sessionId) {
        ChatSession chatSession = findById(sessionId);
        this.groupUserId2SessionId.remove(toGroupUserId(chatSession.getGroupId(), chatSession.getUserName()));
        this.sessionId2ChatSession.remove(sessionId);
    }

    public ChatSession findById(String sessionId) {
        return this.sessionId2ChatSession.getOrDefault(sessionId, null);
    }

    public String findGroupIdBySessionId(String sessionId) {
        return this.sessionId2ChatSession.getOrDefault(sessionId, null)
                .getGroupId();
    }

    public String findUserNameBySessionId(String sessionId) {
        return this.sessionId2ChatSession.getOrDefault(sessionId, null)
                .getUserName();
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

    public ChatSession findByGroupUser(String groupId, String userName) {
        return findByGroupUserId(toGroupUserId(groupId, userName));
    }

    private ChatSession findByGroupUserId(String groupUserId) {
        return findById(this.groupUserId2SessionId.get(groupUserId));
    }

    private String toGroupUserId(String groupId, String userName) {
        return groupId + userName;
    }

}
