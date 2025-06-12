package com.dt.dev.indexingservice.infrastructure.messaging;

import com.dt.dev.documentservice.domain.event.DocumentUploadEvent;
import com.dt.dev.indexingservice.application.service.ollama.EmbeddingService;
import com.dt.dev.indexingservice.application.service.weaviate.WeaviateClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DocumentUploadedListener {

    private final EmbeddingService embeddingService;
    private final WeaviateClient weaviateClient;

    public DocumentUploadedListener(EmbeddingService embeddingService, WeaviateClient weaviateClient) {
        this.embeddingService = embeddingService;
        this.weaviateClient = weaviateClient;
    }

    @KafkaListener(
            topics = "document_uploaded",
            groupId = "indexing-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onMessage(DocumentUploadEvent event) {
        embeddingService.chunkAndEmbed(event)
                .flatMap(weaviateClient::saveChunk)
                .subscribe();
    }
}
