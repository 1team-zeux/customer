package com.demo.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${recommendationAgent.url:http://recommendation-agent:17004}")
    private String recommendationAgentUrl;

    @Bean
    public RestClient recommendationRestClient() {
        return RestClient.builder()
            .baseUrl(recommendationAgentUrl)
            .defaultHeader("Accept", "application/json")
            .build();
    }
}
