package com.dt.dev.queryservice.query.infrastructure.ollama;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class OllamaEmbeddingClient implements EmbeddingGeneratorClient {

    private final WebClient webClient;

    public OllamaEmbeddingClient(@Qualifier("ollamaWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<Double>> generateEmbedding(String text) {
        return webClient.post()
                .uri("/api/embeddings")
                .bodyValue(Map.of("model", "nomic-embed-text", "prompt", text))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> StreamSupport.stream(json.get("embedding").spliterator(), false)
                        .map(JsonNode::doubleValue)
                        .collect(Collectors.toList()));
    }
}
