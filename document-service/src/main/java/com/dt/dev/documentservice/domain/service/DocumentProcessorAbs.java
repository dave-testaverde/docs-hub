package com.dt.dev.documentservice.domain.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public abstract class DocumentProcessorAbs {
    public abstract Mono<Void> process(FilePart filePart);
}
