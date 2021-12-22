package com.example.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
