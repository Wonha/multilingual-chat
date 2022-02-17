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
package com.example.chat.domain;

import com.example.chat.model.MessageRequest;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@Builder
public class Message {

    private String id;
    private int num;
    private Type type;
    private String groupId;
    private User sender;
    private String createdAt;
    private String text;
    private String detectedLanguage;
    private Set<Translation> translatedMessage;

    public enum Type {
        ENTER, TALK, EXIT
    }

    public static Message of(MessageRequest messageRequest, int messageNum) {
        return Message.builder()
                .id(UUID.randomUUID().toString())
                .num(messageNum)
                .type(messageRequest.getType())
                .groupId(messageRequest.getGroupId())
                .sender(messageRequest.getSender())
                .createdAt(Instant.now().toString())
                .text(messageRequest.getText())
                .build();
    }

}
