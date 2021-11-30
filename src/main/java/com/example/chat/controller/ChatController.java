package com.example.chat.controller;

import com.example.chat.domain.ChatGroup;
import com.example.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;

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
