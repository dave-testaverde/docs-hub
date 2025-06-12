package com.dt.dev.indexingservice.shared;

import java.util.List;

public class Normalizer {
    public static List<Double> normalize(List<Double> vector) {
        double norm = Math.sqrt(vector.stream()
                .mapToDouble(v -> v * v)
                .sum());

        if (norm == 0) return vector;

        return vector.stream()
                .map(v -> v / norm)
                .toList();
    }
}
