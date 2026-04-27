package ru.hse.whowantstobeamillionaire.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionSource {
    DATABASE("База данных"),
    AI("ИИ");

    private final String displayName;

    public static QuestionSource getOrDefault(QuestionSource questionSource) {
        if (questionSource == null) {
            return QuestionSource.DATABASE;
        }
        return questionSource;
    }

    public static QuestionSource parseQuestionSource(String questionSource) {
        if (questionSource == null || questionSource.isBlank()) {
            return QuestionSource.DATABASE;
        }

        try {
            return QuestionSource.valueOf(questionSource);
        } catch (IllegalArgumentException ignored) {
            return QuestionSource.DATABASE;
        }
    }
}
