package ru.hse.whowantstobeamillionaire.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class AiQuestionGenerationConfig {

    @Bean
    RestClient.Builder aiQuestionGenerationRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    ObjectMapper aiQuestionGenerationObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    RestClient aiQuestionGenerationRestClient(
            RestClient.Builder restClientBuilder,
            AiQuestionGenerationProperties properties
    ) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.getConnectTimeoutMs());
        requestFactory.setReadTimeout(properties.getReadTimeoutMs());

        return restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .requestFactory(requestFactory)
                .build();
    }
}
