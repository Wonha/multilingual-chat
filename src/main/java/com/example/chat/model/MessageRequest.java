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
package com.example.chat.model;

import com.example.chat.domain.Message;
import com.example.chat.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageRequest {

    private Message.Type type;
    private String groupId;
    private User sender;
    private String text;

    @Builder
    public MessageRequest(Message.Type type, String groupId, User sender) {
        this.type = type;
        this.groupId = groupId;
        this.sender = sender;
    }
}
