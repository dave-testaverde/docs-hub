package com.dt.dev.queryservice.query.infrastructure.weaviate;

import com.dt.dev.indexingservice.domain.model.Chunk;
import com.dt.dev.indexingservice.shared.Normalizer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class WeaviateEmbeddingStoreClient implements EmbeddingStoreClient {

    private final WebClient webClient;

    public WeaviateEmbeddingStoreClient(@Qualifier("weaviateWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<Chunk> findRelevantChunks(List<Double> vector) {
        String vectorJson = Normalizer.normalize(vector).toString();

        // RAG - dense retrieval
        Map<String, Object> body = Map.of(
                "query", """
            {
              Get {
                DocumentChunk(
                  nearVector: {
                    vector: %s,
                    certainty: 0.6
                  },
                  limit: 3
                ) {
                  documentId
                  text
                  _additional {
                    vector
                  }
                }
              }
            }
            """.formatted(vectorJson)
        );

        System.out.println(body);

        return webClient.post()
                .uri("/v1/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMapMany(json -> {
                    System.out.println("FULL RESPONSE: " + json.toPrettyString());
                    JsonNode chunks = json.at("/data/Get/DocumentChunk");
                    if (chunks == null || !chunks.isArray()) return Flux.empty();

                    return Flux.fromStream(StreamSupport.stream(chunks.spliterator(), false)
                            .map(node -> new Chunk(
                                    node.get("documentId").asText(),
                                    node.get("text").asText(),
                                    StreamSupport.stream(node.get("_additional").get("vector").spliterator(), false)
                                            .map(JsonNode::doubleValue)
                                            .collect(Collectors.toList())
                            )));
                });
    }
}
