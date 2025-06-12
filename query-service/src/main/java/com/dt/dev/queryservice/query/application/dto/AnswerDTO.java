package com.dt.dev.queryservice.query.application.dto;

import com.dt.dev.queryservice.query.domain.model.Answer;

public record AnswerDTO(String text) {
    public static AnswerDTO fromDomain(Answer answer) {
        return new AnswerDTO(answer.text());
    }
}
