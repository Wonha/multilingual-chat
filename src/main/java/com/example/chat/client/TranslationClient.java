package com.example.chat.client;

import com.example.chat.config.GcpConfig;
import com.google.cloud.translate.v3.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.example.chat.config.AsyncConfig.TRANSLATION_THREAD_POOL;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationClient {
    private final GcpConfig gcpConfig;

    @Async(TRANSLATION_THREAD_POOL)
    public CompletableFuture<String> translate(String text, String preferredLanguage) {
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            LocationName parent = LocationName.of(gcpConfig.getProjectId(), "global");
            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setTargetLanguageCode(preferredLanguage)
                            .addContents(text)
                            .build();

            TranslateTextResponse response = client.translateText(request);

            return CompletableFuture.completedFuture(
                    response.getTranslationsList().stream()
                            .map(Translation::getTranslatedText)
                            .collect(Collectors.joining()));

        } catch (IOException e) {
            log.warn("Translation failed {}", e);
            return CompletableFuture.completedFuture("");
        }
    }
}
