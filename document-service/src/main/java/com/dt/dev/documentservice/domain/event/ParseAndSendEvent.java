package com.dt.dev.documentservice.domain.event;

import reactor.core.publisher.Mono;

import java.io.File;

public interface ParseAndSendEvent {
    Mono<Void> parseAndSend(File file);
}
