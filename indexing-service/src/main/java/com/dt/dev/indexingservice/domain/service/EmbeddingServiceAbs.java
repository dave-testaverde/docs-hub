package com.dt.dev.indexingservice.domain.service;

import com.dt.dev.documentservice.domain.event.DocumentUploadEvent;
import com.dt.dev.indexingservice.domain.model.Chunk;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public abstract class EmbeddingServiceAbs {
    public abstract Flux<Chunk> chunkAndEmbed(DocumentUploadEvent event);
    protected abstract Mono<List<Double>> generateEmbedding(String input);
}
