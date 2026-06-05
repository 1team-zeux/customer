package com.demo.order.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationClient {

    private final RestClient recommendationRestClient;

    @SuppressWarnings("unchecked")
    public Object getRecommendations(String userId) {
        try {
            return recommendationRestClient.get()
                .uri("/recommendations/{userId}", userId)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.warn("Recommendation agent unavailable for user {}: {}", userId, e.getMessage());
            return Map.of("recommendations", List.of(), "error", "agent unavailable");
        }
    }
}
