package ru.hse.whowantstobeamillionaire.service;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import ru.hse.whowantstobeamillionaire.config.AiQuestionGenerationProperties;
import ru.hse.whowantstobeamillionaire.model.Question;

@Service
public class AiQuestionGenerationService {
    private static final String BASIC_AUTH_PREFIX = "Basic ";
    private static final String BEARER_AUTH_PREFIX = "Bearer ";

    private final RestClient aiQuestionGenerationRestClient;
    private final RestClient aiQuestionGenerationAuthRestClient;
    private final AiQuestionGenerationProperties properties;
    private final ObjectMapper objectMapper;

    public AiQuestionGenerationService(
            @Qualifier("aiQuestionGenerationRestClient") RestClient aiQuestionGenerationRestClient,
            @Qualifier("aiQuestionGenerationAuthRestClient") RestClient aiQuestionGenerationAuthRestClient,
            AiQuestionGenerationProperties properties,
            ObjectMapper objectMapper
    ) {
        this.aiQuestionGenerationRestClient = aiQuestionGenerationRestClient;
        this.aiQuestionGenerationAuthRestClient = aiQuestionGenerationAuthRestClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public Question generateQuestion(int level) {
        validateRequest(level);
        String accessToken = fetchAccessToken();

        ChatCompletionResponse response = aiQuestionGenerationRestClient.post()
                .uri("/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, BEARER_AUTH_PREFIX + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(buildRequest(level))
                .retrieve()
                .body(ChatCompletionResponse.class);

        System.out.println(response);
        return toQuestion(level, response);
    }

    private void validateRequest(int level) {
        if (level <= 0) {
            throw new IllegalArgumentException("Уровень вопроса должен быть положительным.");
        }
    }

    private String fetchAccessToken() {
        OAuthTokenResponse response = aiQuestionGenerationAuthRestClient.post()
                .uri(properties.getAuthUrl())
                .header(HttpHeaders.AUTHORIZATION, buildBasicAuthorizationHeader())
                .header("RqUID", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(buildAuthRequestBody())
                .retrieve()
                .body(OAuthTokenResponse.class);

        if (response == null || isBlank(response.accessToken())) {
            throw new IllegalStateException("GigaChat auth service returned no access token.");
        }

        return response.accessToken().trim();
    }

    private MultiValueMap<String, String> buildAuthRequestBody() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("scope", properties.getScope());
        return form;
    }

    private String buildBasicAuthorizationHeader() {
        String authorizationKey = properties.getAuthorizationKey().trim();
        if (authorizationKey.startsWith(BASIC_AUTH_PREFIX)) {
            return authorizationKey;
        }
        return BASIC_AUTH_PREFIX + authorizationKey;
    }

    private ChatCompletionRequest buildRequest(int level) {
        String systemPrompt = "Ты генерируешь вопросы для игры 'Кто хочет стать миллионером'. "
                + "Верни строго JSON без markdown, пояснений и лишнего текста. "
                + "Формат json: "
                + "{\"text\":\"...\",\"options\":[\"...\",\"...\",\"...\",\"...\"],\"correctOptionIndex\":1}. "
                + "Поле correctOptionIndex должно быть числом от 1 до 4. "
                + "В options должно быть ровно 4 непустых варианта, только один правильный. "
                + "Вопрос и все варианты ответа должны быть на русском языке.";
        String userPrompt = "Сгенерируй один вопрос уровня сложности " + level + " из 15.";

        return new ChatCompletionRequest(
                properties.getModel(),
                List.of(
                        new Message("system", systemPrompt),
                        new Message("user", userPrompt)
                ),
                properties.getTemperature(),
                properties.getMaxOutputTokens(),
                false
        );
    }

    private Question toQuestion(int level, ChatCompletionResponse response) {
        String rawJson = extractResponseText(response);
        GeneratedQuestion generatedQuestion = parseGeneratedQuestion(rawJson);
        validateGeneratedQuestion(generatedQuestion);

        return new Question(
                generatedQuestion.text().trim(),
                generatedQuestion.options().stream().map(String::trim).toList(),
                generatedQuestion.correctOptionIndex(),
                level
        );
    }

    private String extractResponseText(ChatCompletionResponse response) {
        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            throw new IllegalStateException("AI service returned no choices.");
        }

        Choice choice = response.choices().get(0);
        if (choice == null || choice.message() == null) {
            throw new IllegalStateException("AI service returned an empty choice.");
        }

        String content = choice.message().content();
        if (isBlank(content)) {
            throw new IllegalStateException("AI service returned no text content.");
        }

        return content;
    }

    private GeneratedQuestion parseGeneratedQuestion(String rawJson) {
        try {
            return objectMapper.readValue(rawJson, GeneratedQuestion.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse AI question JSON.", e);
        }
    }

    private void validateGeneratedQuestion(GeneratedQuestion generatedQuestion) {
        if (generatedQuestion == null) {
            throw new IllegalStateException("AI service returned an empty question.");
        }
        if (isBlank(generatedQuestion.text())) {
            throw new IllegalStateException("AI question text is empty.");
        }
        if (generatedQuestion.options() == null || generatedQuestion.options().size() != 4) {
            throw new IllegalStateException("AI question must contain exactly four options.");
        }
        if (generatedQuestion.options().stream().anyMatch(this::isBlank)) {
            throw new IllegalStateException("AI question options must not be blank.");
        }
        int correctOptionIndex = generatedQuestion.correctOptionIndex();
        if (correctOptionIndex < 1 || correctOptionIndex > 4) {
            throw new IllegalStateException("AI question correctOptionIndex must be between 1 and 4.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private record ChatCompletionRequest(
            String model,
            List<Message> messages,
            double temperature,
            @JsonProperty("max_tokens") int maxTokens,
            boolean stream
    ) {
    }

    private record Message(String role, String content) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ChatCompletionResponse(List<Choice> choices) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Choice(ChatMessage message, @JsonProperty("finish_reason") String finishReason) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ChatMessage(String role, String content) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record OAuthTokenResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("expires_at") Long expiresAt
    ) {
    }

    private record GeneratedQuestion(String text, List<String> options, int correctOptionIndex) {
    }
}
