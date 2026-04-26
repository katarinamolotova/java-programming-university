package ru.hse.whowantstobeamillionaire.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LifelineType {
    AUDIENCE_HELP("Помощь зала"),
    FIFTY_FIFTY("50/50"),
    PHONE_FRIEND("Звонок другу"),
    RIGHT_TO_ERROR("Право на ошибку"),
    REPLACE_QUESTION("Замена вопроса");

    private final String name;
}
