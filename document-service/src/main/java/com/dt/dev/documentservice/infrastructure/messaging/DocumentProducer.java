package com.dt.dev.documentservice.infrastructure.messaging;

import com.dt.dev.documentservice.domain.event.DocumentUploadEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DocumentProducer {

    private final KafkaTemplate<String, DocumentUploadEvent> kafkaTemplate;

    public DocumentProducer(KafkaTemplate<String, DocumentUploadEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(DocumentUploadEvent event) {
        kafkaTemplate.send("document_uploaded", event);
    }
}
