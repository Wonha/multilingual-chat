package com.example.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerspectiveRequest {

    private Comment comment;
    private Attributes requestedAttributes;

    @Builder
    public PerspectiveRequest(String text) {
        Comment comment = new Comment();
        comment.setText(text);
        this.comment = comment;

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
