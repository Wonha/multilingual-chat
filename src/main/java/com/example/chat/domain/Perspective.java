package com.example.chat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class Perspective {

    private AttributeScore attributeScores;
    private List<String> languages;
    private List<String> detectedLanguages;

    @Getter
    public static class AttributeScore {

        @JsonProperty("TOXICITY")
        private Toxicity toxicity;
    }

    @Getter
    public static class Toxicity {

        private List<SpanScores> spanScores;
        private Score summaryScore;
    }

    @Getter
    public static class SpanScores {

        private int begin;
        private int end;
        private Score score;
    }

    @Getter
    public static class Score {

        private BigDecimal value;
        private String type;
    }

    public BigDecimal getSummaryScore() {
        return this.getAttributeScores().getToxicity().getSummaryScore().getValue();
    }
}
