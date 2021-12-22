package com.example.chat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Configuration
@Getter
@EnableAsync
public class ServerConfig {

    public static final String SYSTEM_NAME = "System";
    public static final String SYSTEM_LANG = "en";
    public final BigDecimal toxicityThreshold;

    public ServerConfig(
            @Value("${perspective.threshold.toxicity}") BigDecimal toxicityThreshold) {
        this.toxicityThreshold = toxicityThreshold;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Autowired
    public void configureJackson(ObjectMapper jackson2ObjectMapper) {
        jackson2ObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
