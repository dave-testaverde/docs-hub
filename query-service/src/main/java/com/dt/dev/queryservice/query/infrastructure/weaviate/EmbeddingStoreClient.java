package com.dt.dev.queryservice.query.infrastructure.weaviate;

import com.dt.dev.indexingservice.domain.model.Chunk;
import reactor.core.publisher.Flux;

import java.util.List;

public interface EmbeddingStoreClient {
    Flux<Chunk> findRelevantChunks(List<Double> vector);
}
