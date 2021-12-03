package com.example.chat.controller;

import com.example.chat.model.ChatGroup;
import com.example.chat.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final GroupService chatService;

    @PostMapping("/chats/groups")
    public ChatGroup createGroup(@RequestParam String name) {
        return chatService.createGroup(name);
    }

    @GetMapping("/chats/groups")
    public List<ChatGroup> findAllGroups() {
        return chatService.findAllGroups();
    }

    @GetMapping("/chats/groups/{groupId}")
    public List<ChatGroup> findGroupById(@PathVariable String groupId) {
        return null;
    }
}
