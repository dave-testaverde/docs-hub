package com.dt.dev.documentservice.interfaces.rest;

import com.dt.dev.documentservice.application.service.DocumentProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentProcessor processor;

    public DocumentController(DocumentProcessor processor) {
        this.processor = processor;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> uploadDocument(@RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono
                .flatMap(processor::process)
                .then(Mono.fromCallable(() -> ResponseEntity.ok("Uploaded and processed")))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error: " + e.getMessage())));
    }
}