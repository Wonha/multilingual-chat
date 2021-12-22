package com.example.chat.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(of = "lang")
@Builder
public class Translation {

    private String lang;
    private String text;
    private boolean isOriginal;
    private BigDecimal toxicityScore;

    public void detoxifyText(BigDecimal toxicityScore, BigDecimal toxicityThreshold) {
        this.toxicityScore = toxicityScore;
        if (toxicityScore.compareTo(toxicityThreshold) >= 1) {
            this.setText("Antidote required: " + this.getText());
        }
    }

}
