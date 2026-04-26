package ru.hse.whowantstobeamillionaire.model;

import java.util.List;

public record Question(
        String text,
        List<String> options,
        int correctOptionIndex,
        int level
) {
}
