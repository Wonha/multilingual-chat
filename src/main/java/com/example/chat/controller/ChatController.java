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
