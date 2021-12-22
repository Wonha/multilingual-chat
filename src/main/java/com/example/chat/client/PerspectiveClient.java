package com.example.chat.client;

import com.example.chat.domain.Perspective;
import com.example.chat.model.PerspectiveRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Component
public class PerspectiveClient {

    private final RestTemplate restTemplate;
    private final String perspectiveUri;
    private final String perspectiveKey;

    public PerspectiveClient(
            RestTemplate restTemplate,
            @Value("${perspective.key}") String perspectiveKey,
            @Value("${perspective.uri}") String perspectiveUri) {

        this.restTemplate = restTemplate;
        this.perspectiveKey = perspectiveKey;
        this.perspectiveUri = perspectiveUri;
    }

    public Perspective perspective(String text, String lang) {
        HttpEntity<PerspectiveRequest> request = new HttpEntity<>(new PerspectiveRequest(text, lang));
        final URI uri = restTemplate.getUriTemplateHandler().expand(this.perspectiveUri, perspectiveKey);

        ResponseEntity<Perspective> response = this.restTemplate.postForEntity(uri, request, Perspective.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Perspective failed {} {} {}",
                    response.getStatusCode(), response.getHeaders(), response.getBody());
            return null;
        }

        log.debug("Perspective >>");
        return response.getBody();
    }
}
