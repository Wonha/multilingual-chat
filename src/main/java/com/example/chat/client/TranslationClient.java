package com.example.chat.client;

import com.google.cloud.translate.v3.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TranslationClient {
    String projectId = "m10lchat-antidote-demo";

    public String translate(String text, String preferredLanguage) {
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            LocationName parent = LocationName.of(projectId, "global");
            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setTargetLanguageCode(preferredLanguage)
                            .addContents(text)
                            .build();

            TranslateTextResponse response = client.translateText(request);

            return response.getTranslationsList().stream()
                    .map(Translation::getTranslatedText)
                    .collect(Collectors.joining());

        } catch (IOException e) {
            log.warn("Translation failed {}", e);
            return "";
        }
    }
}
