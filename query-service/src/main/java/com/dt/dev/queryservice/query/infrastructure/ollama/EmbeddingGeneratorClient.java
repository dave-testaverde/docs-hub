package com.dt.dev.queryservice.query.infrastructure.ollama;

import reactor.core.publisher.Mono;

import java.util.List;

public interface EmbeddingGeneratorClient {
    Mono<List<Double>> generateEmbedding(String text);
}
