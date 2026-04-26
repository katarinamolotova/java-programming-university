package ru.hse.whowantstobeamillionaire.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ai.question-generation")
public class AiQuestionGenerationProperties {
    private String baseUrl = "https://api.deepseek.com";
    private String model = "deepseek-v4-flash";
    private String apiKey;
    private int connectTimeoutMs = 5_000;
    private int readTimeoutMs = 20_000;
    private double temperature = 0.7;
    private int maxOutputTokens = 256;
}
