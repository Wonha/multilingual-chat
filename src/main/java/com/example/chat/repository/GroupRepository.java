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

import com.example.chat.domain.ChatGroup;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GroupRepository {

    private Map<String, ChatGroup> id2Group = new HashMap<>();

    public List<ChatGroup> findAll() {
        return new ArrayList<>(this.id2Group.values());
    }

    public ChatGroup findById(String id) {
        return this.id2Group.get(id);
    }

    public ChatGroup save(ChatGroup group) {
        this.id2Group.put(group.getId(), group);
        return group;
    }

    public void removeById(String id) {
        this.id2Group.remove(id);
    }
}
