package com.dt.dev.indexingservice.application.service.weaviate;

import com.dt.dev.indexingservice.domain.model.Chunk;
import com.dt.dev.indexingservice.shared.Normalizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeaviateClient {

    @Qualifier("weaviateClient")
    private final WebClient webClient;

    public WeaviateClient(@Value("${vector.db.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<Void> saveChunk(Chunk chunk) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("documentId", chunk.getDocumentId());
        properties.put("text", chunk.getText());

        Map<String, Object> payload = new HashMap<>();
        payload.put("class", "DocumentChunk");
        payload.put("properties", properties);
        payload.put("vector", Normalizer.normalize(chunk.getEmbedding()));

        return webClient.post()
                .uri("/v1/objects")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
