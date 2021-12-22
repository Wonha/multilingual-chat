package com.example.chat.controller;

import com.example.chat.domain.ChatGroup;
import com.example.chat.service.GroupService;
import com.example.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@CrossOrigin
@RestController
public class ChatController {

    private final GroupService groupService;
    private final MessageService messageService;

    @PostMapping("/chats/groups")
    public ChatGroup createGroup(@RequestParam String name) {
        return this.groupService.createGroup(name);
    }

    @GetMapping("/chats/groups")
    public List<ChatGroup> findAllGroups() {
        return this.groupService.findAllGroups();
    }

    @GetMapping("/chats/groups/{groupId}")
    public ChatGroup findGroupById(@PathVariable String groupId) {
        return this.groupService.findById(groupId);
    }

    @DeleteMapping("/chats/groups/{groupId}")
    public ChatGroup deleteGroupById(@PathVariable String groupId) {
        return this.messageService.deleteGroupById(groupId);
    }
}
