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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class PerspectiveRequest {

    private Comment comment;
    private List<String> languages;
    private Attributes requestedAttributes;

    @Builder
    public PerspectiveRequest(String text, String lang) {
        Comment comment = new Comment();
        comment.setText(text);
        this.comment = comment;

        this.languages = Arrays.asList(lang);

        Toxicity toxicity = new Toxicity();
        Attributes attributes = new Attributes();
        attributes.setToxicity(toxicity);
        this.requestedAttributes = attributes;
    }

    @Getter
    @Setter
    public static class Comment {

        private String text;
    }

    @Getter
    @Setter
    public static class Attributes {

        @JsonProperty("TOXICITY")
        private Toxicity toxicity;
    }

    @Getter
    @Setter
    public static class Toxicity {
    }
}
