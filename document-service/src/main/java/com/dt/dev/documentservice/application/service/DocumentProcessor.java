package com.dt.dev.documentservice.application.service;

import com.dt.dev.documentservice.domain.event.ParseAndSendEvent;
import com.dt.dev.documentservice.domain.service.DocumentProcessorAbs;
import com.dt.dev.documentservice.infrastructure.messaging.DocumentProducer;
import com.dt.dev.documentservice.domain.event.DocumentUploadEvent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class DocumentProcessor extends DocumentProcessorAbs implements ParseAndSendEvent {

    private final DocumentProducer producer;

    public DocumentProcessor(DocumentProducer producer) {
        this.producer = producer;
    }

    public Mono<Void> process(FilePart filePart) {
        return Mono.fromCallable(() -> Files.createTempFile("upload-", ".pdf"))
                .flatMap(tempPath ->
                    DataBufferUtils.write(filePart.content(), tempPath)
                        .then(Mono.defer(() -> parseAndSend(tempPath.toFile())))
                        .doFinally(signal -> tempPath.toFile().delete()) // Cleanup
                );
    }

    public Mono<Void> parseAndSend(File file) {
        return Mono.fromCallable(() -> {
            try (InputStream input = new FileInputStream(file);
                 PDDocument pdfDoc = PDDocument.load(input)) {

                String text = new PDFTextStripper().getText(pdfDoc);
                String docId = UUID.randomUUID().toString();
                DocumentUploadEvent event = new DocumentUploadEvent(docId, text);
                producer.send(event);
                return true;
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}