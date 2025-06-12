package com.dt.dev.indexingservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Chunk {
    private String documentId;
    private String text;
    private List<Double> embedding;
}
