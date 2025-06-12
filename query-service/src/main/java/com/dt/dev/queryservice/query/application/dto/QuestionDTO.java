package com.dt.dev.queryservice.query.application.dto;

import com.dt.dev.queryservice.query.domain.model.Question;

public record QuestionDTO(String text) {
    public Question toDomain() {
        return new Question(text);
    }
}
