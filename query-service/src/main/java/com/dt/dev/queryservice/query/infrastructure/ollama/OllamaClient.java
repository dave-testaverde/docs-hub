package com.dt.dev.queryservice.query.infrastructure.ollama;

import com.dt.dev.queryservice.query.domain.model.OllamaChunk;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OllamaClient implements LLMClient {

    private final WebClient webClient;

    public OllamaClient(@Qualifier("ollamaWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> ask(String prompt) {
        System.out.println("Prompt: " + prompt);

        return webClient.post()
                .uri("/api/generate")
                .bodyValue(Map.of("model", "llama3", "prompt", prompt))
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(OllamaChunk.class)
                .map(OllamaChunk::getResponse)
                .collect(Collectors.joining());
    }
}
