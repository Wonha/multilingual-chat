package com.example.chat.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@Getter
@EnableAsync
public class ServerConfig {

    public final String uriTranslationApi;

    public static final String SYSTEM_NAME = "System";
    public static final String SYSTEM_LANG = "en";

    public ServerConfig(
            @Value("${translation.uri}") String uriTranslationApi) {
        this.uriTranslationApi = uriTranslationApi;
    }
}
