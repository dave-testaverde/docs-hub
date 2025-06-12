package com.dt.dev.queryservice.query.infrastructure.ollama;

import reactor.core.publisher.Mono;

public interface LLMClient {
    Mono<String> ask(String prompt);
}
