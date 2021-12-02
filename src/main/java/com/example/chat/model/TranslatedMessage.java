package com.example.chat.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "lang")
public class TranslatedMessage {
    private String lang;
    private String text;

}
