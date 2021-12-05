package com.example.chat.model;

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

    @Builder
    public MessageWithLanguage(String lang, String text) {
        this.lang = lang;
        this.text = text;
    }
}
