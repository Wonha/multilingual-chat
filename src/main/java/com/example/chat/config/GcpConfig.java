package com.example.chat.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class GcpConfig {
    public final String projectId;

    public GcpConfig(
            @Value("${gcp.project-id}") String projectId) {
        this.projectId = projectId;
    }
}
