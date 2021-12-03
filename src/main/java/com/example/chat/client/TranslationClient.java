package com.example.chat.client;

import com.example.chat.config.GcpConfig;
import com.google.cloud.translate.v3.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
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

            log.debug("Translation >>");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    @Async(TRANSLATION_THREAD_POOL)
    public CompletableFuture<String> detectLanguage(String text) {
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            LocationName parent = LocationName.of(gcpConfig.getProjectId(), "global");

            DetectLanguageRequest request =
                    DetectLanguageRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setContent(text)
                            .build();

            log.debug("Detection >>");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            DetectLanguageResponse response = client.detectLanguage(request);

            for (DetectedLanguage language : response.getLanguagesList()) {
                // The language detected
                System.out.printf("Language code: %s\n", language.getLanguageCode());
                // Confidence of detection result for this language
                System.out.printf("Confidence: %s\n", language.getConfidence());
            }
            return CompletableFuture.completedFuture(
                    response.getLanguagesList().stream()
                            .min((l1, l2) -> l1.getConfidence() > l2.getConfidence() ? 1 : 0)
                            .map(DetectedLanguage::getLanguageCode)
                            .orElse(""));
        } catch (IOException e) {
            log.warn("Language Detection failed {}", e);
            return CompletableFuture.completedFuture("unknown");
        }


    }
}
