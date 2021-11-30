package com.example.chat.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "preferredLanguage")
public class TranslatedMessage {
    private String language;
    private String text;
}
