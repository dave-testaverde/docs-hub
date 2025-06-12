package com.dt.dev.queryservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${vector.db.url}")
    private String vectorDbUrl;

    @Value("${ollama.url}")
    private String ollamaUrl;

    @Bean
    @Qualifier("weaviateWebClient")
    public WebClient weaviateWebClient() {
        return WebClient.builder()
                .baseUrl(vectorDbUrl)
                .build();
    }

    @Bean
    @Qualifier("ollamaWebClient")
    public WebClient ollamaWebClient() {
        return WebClient.builder()
                .baseUrl(ollamaUrl)
                .build();
    }
}
