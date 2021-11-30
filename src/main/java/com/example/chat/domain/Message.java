package com.example.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class Message {

    private String id;
    public enum Type {
        ENTER, TALK, EXIT
    }
    private Type type;
    private String groupId;
    private User sender;

    private String text;
    private Set<TranslatedMessage> translatedMessage;
    @JsonIgnore
    private String disruptiveScore;

}
