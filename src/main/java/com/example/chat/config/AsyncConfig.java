package com.example.chat.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@Configuration
@EnableAsync
@Getter
public class AsyncConfig {
    private final int mainPoolCoreSize;
    private final int mainPoolMaxSize;
    private final int mainPoolQueueSize;

    public static final String TRANSLATION_THREAD_POOL = "translation-thread-pool";

    public AsyncConfig(
            @Value("${thread-pool.main.size.max}") int mainPoolMaxSize,
            @Value("${thread-pool.main.size.queue}") int mainPoolQueueSize,
            @Value("${thread-pool.main.size.core}") int mainPoolCoreSize) {
        this.mainPoolCoreSize = mainPoolCoreSize;
        this.mainPoolMaxSize = mainPoolMaxSize;
        this.mainPoolQueueSize = mainPoolQueueSize;
    }

    @Bean(name = TRANSLATION_THREAD_POOL)
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(getMainPoolCoreSize());
        executor.setMaxPoolSize(getMainPoolMaxSize());
        executor.setQueueCapacity(getMainPoolQueueSize());
        executor.initialize();
        return executor;
    }
}
