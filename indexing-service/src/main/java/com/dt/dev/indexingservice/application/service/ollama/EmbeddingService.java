package com.dt.dev.indexingservice.application.service.ollama;

import com.dt.dev.documentservice.domain.event.DocumentUploadEvent;
import com.dt.dev.indexingservice.domain.model.Chunk;
import com.dt.dev.indexingservice.domain.model.OllamaEmbeddingResponse;
import com.dt.dev.indexingservice.domain.service.EmbeddingServiceAbs;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class EmbeddingService extends EmbeddingServiceAbs {

    private final WebClient webClient;

    public EmbeddingService(@Value("${ollama.url}") String ollamaUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(ollamaUrl)
                .build();
    }

    public Flux<Chunk> chunkAndEmbed(DocumentUploadEvent event) {
        String text = event.getContent();
        String documentId = event.getDocumentId();
        int chunkSize = 1000;

        return Flux.range(0, (int) Math.ceil((double) text.length() / chunkSize))
                .map(i -> {
                    int start = i * chunkSize;
                    int end = Math.min(start + chunkSize, text.length());
                    return StringUtils.mid(text, start, end - start);
                })
                .flatMap(chunkText -> generateEmbedding(chunkText)
                        .map(embedding -> new Chunk(documentId, chunkText, embedding)));
    }

    protected Mono<List<Double>> generateEmbedding(String input) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "nomic-embed-text"); // or mxbai-embed-large
        payload.put("prompt", input);

        return webClient.post()
                .uri("/api/embeddings")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(OllamaEmbeddingResponse.class)
                .map(OllamaEmbeddingResponse::getEmbedding);
    }
}
