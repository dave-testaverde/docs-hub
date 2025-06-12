package com.dt.dev.queryservice.query.infrastructure.rest;

import com.dt.dev.queryservice.query.application.dto.AnswerDTO;
import com.dt.dev.queryservice.query.application.dto.QuestionDTO;
import com.dt.dev.queryservice.query.application.service.QueryProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/query")
@RequiredArgsConstructor
public class QueryHandler {
    private final QueryProcessorService queryProcessorService;

    @PostMapping
    public Mono<AnswerDTO> ask(@RequestBody Mono<QuestionDTO> dtoMono) {
        return dtoMono
                .flatMap(dto -> queryProcessorService.processQuestion(dto.toDomain()))
                .map(AnswerDTO::fromDomain);
    }
}
