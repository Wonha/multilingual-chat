package com.example.chat.controller;

import com.example.chat.model.ChatGroup;
import com.example.chat.service.ChatGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatGroupService chatService;

    @PostMapping("/chats")
    public ChatGroup createGroup(@RequestParam String name) {
        return chatService.createGroup(name);
    }

    @GetMapping("/chats")
    public List<ChatGroup> findAllGroups() {
        return chatService.findAllGroups();
    }

    @GetMapping("/chats/{id}")
    public List<ChatGroup> findChatByGroupId(@PathVariable String id) {
        return null;
    }
}
