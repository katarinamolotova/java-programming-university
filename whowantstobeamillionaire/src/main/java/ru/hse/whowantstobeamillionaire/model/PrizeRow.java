package ru.hse.whowantstobeamillionaire.model;

public record PrizeRow(
        int level,
        int amount,
        boolean current
) {
}