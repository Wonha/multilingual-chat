package com.example.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "lang")
@Builder
public class Translation {

    private String lang;
    private String text;
    @JsonIgnore
    private boolean isOriginal;
}
