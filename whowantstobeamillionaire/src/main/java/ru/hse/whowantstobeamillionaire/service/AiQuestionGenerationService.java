package ru.hse.whowantstobeamillionaire.service;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.hse.whowantstobeamillionaire.config.AiQuestionGenerationProperties;
import ru.hse.whowantstobeamillionaire.model.Question;

@Service
@RequiredArgsConstructor
public class AiQuestionGenerationService {
    private static final String JSON_OBJECT_RESPONSE_TYPE = "json_object";
    private static final String THINKING_DISABLED = "disabled";

    private final RestClient aiQuestionGenerationRestClient;
    private final AiQuestionGenerationProperties properties;
    private final ObjectMapper objectMapper;

    public Question generateQuestion(int level) {
        validateRequest(level);

        ChatCompletionResponse response = aiQuestionGenerationRestClient.post()
                .uri("/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getApiKey().trim())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(buildRequest(level))
                .retrieve()
                .body(ChatCompletionResponse.class);

        return toQuestion(level, response);
    }

    private void validateRequest(int level) {
        if (level <= 0) {
            throw new IllegalArgumentException("Уровень вопроса должен быть положительным.");
        }
    }

    private ChatCompletionRequest buildRequest(int level) {
        String systemPrompt = "Ты генерируешь вопросы для игры 'Кто хочет стать миллионером'. "
                + "Верни строго valid json object без markdown, пояснений и лишнего текста. "
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
                new ResponseFormat(JSON_OBJECT_RESPONSE_TYPE),
                new Thinking(THINKING_DISABLED),
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

    private record ChatCompletionRequest(String model,
                                         List<Message> messages,
                                         double temperature,
                                         @JsonProperty("max_tokens") int maxTokens,
                                         @JsonProperty("response_format") ResponseFormat responseFormat,
                                         Thinking thinking,
                                         boolean stream) {
    }

    private record Message(String role, String content) {
    }

    private record ResponseFormat(String type) {
    }

    private record Thinking(String type) {
    }

    private record ChatCompletionResponse(List<Choice> choices) {
    }

    private record Choice(ChatMessage message, @JsonProperty("finish_reason") String finishReason) {
    }

    private record ChatMessage(String role, String content) {
    }

    private record GeneratedQuestion(String text, List<String> options, int correctOptionIndex) {
    }
}
