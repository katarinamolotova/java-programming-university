package ru.hse.whowantstobeamillionaire.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "ai.question-generation")
public class AiQuestionGenerationProperties {
    private String baseUrl = "https://gigachat.devices.sberbank.ru/api/v1";
    private String authUrl = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth";
    private String model = "GigaChat-2-Lite";
    private String authorizationKey;
    private String scope = "GIGACHAT_API_PERS";
    private int connectTimeoutMs = 5_000;
    private int readTimeoutMs = 20_000;
    private double temperature = 0.7;
    private int maxOutputTokens = 256;
}
