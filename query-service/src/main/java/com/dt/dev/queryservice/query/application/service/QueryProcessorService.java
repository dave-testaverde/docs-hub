package com.dt.dev.queryservice.query.application.service;

import com.dt.dev.indexingservice.domain.model.Chunk;
import com.dt.dev.queryservice.query.domain.model.Answer;
import com.dt.dev.queryservice.query.domain.model.Question;
import com.dt.dev.queryservice.query.domain.service.QueryProcessorServiceAbs;
import com.dt.dev.queryservice.query.infrastructure.ollama.EmbeddingGeneratorClient;
import com.dt.dev.queryservice.query.infrastructure.ollama.LLMClient;
import com.dt.dev.queryservice.query.infrastructure.weaviate.EmbeddingStoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryProcessorService extends QueryProcessorServiceAbs {
    private final EmbeddingStoreClient embeddingStoreClient;
    private final EmbeddingGeneratorClient embeddingGeneratorClient;
    private final LLMClient llmClient;

    private final String LANGUAGE = "italiano";

    public Mono<Answer> processQuestion(Question question) {
        return embeddingGeneratorClient.generateEmbedding(question.text())
                .flatMapMany(embedding -> embeddingStoreClient.findRelevantChunks(embedding))
                .collectList()
                .flatMap(chunks -> {
                    String context = chunks.stream()
                            .map(Chunk::getText)
                            .collect(Collectors.joining("\n\n"));

                    String prompt = buildPrompt(context, question.text());
                    return llmClient.ask(prompt).map(Answer::new);
                });
    }

    protected String buildPrompt(String context, String question) {
        return "Contesto:\n%s\n\nDomanda:\n%s\n\nRisposta in %s:".formatted(context, question, LANGUAGE);
    }
}
