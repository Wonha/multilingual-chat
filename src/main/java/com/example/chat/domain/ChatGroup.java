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

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ChatGroup {

    private String id;
    private String name;
    private int messageNum;
    private Set<User> users = new HashSet<>();

    @Builder
    public ChatGroup(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Set<String> findPreferredLanguages() {
        return this.getUsers().stream()
                .map(User::getLang)
                .collect(Collectors.toSet());
    }

    public boolean hasNoUser() {
        return this.getUsers().size() <= 0;
    }

    public int increaseAndGetMessageNum() {
        return ++this.messageNum;
    }
}
