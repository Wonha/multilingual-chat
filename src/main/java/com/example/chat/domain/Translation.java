/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
