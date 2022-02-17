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
