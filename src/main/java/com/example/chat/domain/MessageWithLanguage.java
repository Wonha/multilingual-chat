package com.example.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "lang")
public class MessageWithLanguage {
    private String lang;
    private String text;
    @JsonIgnore
    private boolean isOriginal;

    @Builder
    public MessageWithLanguage(String lang, String text, boolean isOriginal) {
        this.lang = lang;
        this.text = text;
        this.isOriginal = isOriginal;
    }
}
