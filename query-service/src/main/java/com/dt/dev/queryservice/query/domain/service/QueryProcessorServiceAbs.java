package com.dt.dev.queryservice.query.domain.service;

import com.dt.dev.queryservice.query.domain.model.Answer;
import com.dt.dev.queryservice.query.domain.model.Question;
import reactor.core.publisher.Mono;

public abstract class QueryProcessorServiceAbs {
    public abstract Mono<Answer> processQuestion(Question question);
    protected abstract String buildPrompt(String context, String question);
}
