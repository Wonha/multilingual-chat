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
import com.example.chat.domain.Translation;
import com.example.chat.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class MessageDisplay {

    private String id;
    private int num;
    private String lang;
    private String text;
    private Message.Type type;
    private String createdAt;
    private User sender;
    private Set<Translation> translations;

    @Builder
    public MessageDisplay(
            String id,
            int num,
            String lang,
            String text,
            Message.Type type,
            String createdAt,
            User sender,
            Set<Translation> translations) {
        this.id = id;
        this.num = num;
        this.lang = lang;
        this.text = text;
        this.type = type;
        this.createdAt = createdAt;
        this.sender = sender;
        this.translations = translations;
    }

    public static MessageDisplay of(Message message, Translation original) {
        return MessageDisplay.builder()
                .id(message.getId())
                .num(message.getNum())
                .lang(original.getLang())
                .text(original.getText())
                .type(message.getType())
                .sender(message.getSender())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
