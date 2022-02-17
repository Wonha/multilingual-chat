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
    private final String mainPoolPrefix;

    public static final String ASYNC_CLIENT_THREAD_POOL = "translation-thread-pool";

    public AsyncConfig(
            @Value("${thread-pool.main.size.max}") int mainPoolMaxSize,
            @Value("${thread-pool.main.size.queue}") int mainPoolQueueSize,
            @Value("${thread-pool.main.size.core}") int mainPoolCoreSize,
            @Value("${thread-pool.main.prefix}") String mainPoolPrefix) {
        this.mainPoolCoreSize = mainPoolCoreSize;
        this.mainPoolMaxSize = mainPoolMaxSize;
        this.mainPoolQueueSize = mainPoolQueueSize;
        this.mainPoolPrefix = mainPoolPrefix;
    }

    @Bean(name = ASYNC_CLIENT_THREAD_POOL)
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(getMainPoolCoreSize());
        executor.setMaxPoolSize(getMainPoolMaxSize());
        executor.setQueueCapacity(getMainPoolQueueSize());
        executor.setThreadNamePrefix(getMainPoolPrefix());
        executor.initialize();
        return executor;
    }
}
