package ru.hse.whowantstobeamillionaire.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class GameSession {
    private final List<Question> questions;
    private final String playerName;
    private final int guaranteedAmount;
    private final EnumSet<LifelineType> usedLifelines = EnumSet.noneOf(LifelineType.class);
    private final Set<Integer> eliminatedOptions = new HashSet<>();

    private int currentQuestionIndex;
    private boolean finished;
    private boolean won;
    private boolean tookMoney;
    private boolean rightToErrorActive;
    private int wonAmount;
    private String message;
    private String hint;
    private boolean recordSaved;

    public GameSession(List<Question> questions, String playerName, int guaranteedAmount) {
        this.questions = new ArrayList<>(questions);
        this.playerName = playerName;
        this.guaranteedAmount = guaranteedAmount;
        this.currentQuestionIndex = 0;
    }

    public Question getCurrentQuestion() {
        if (finished || currentQuestionIndex >= questions.size()) {
            return null;
        }
        return questions.get(currentQuestionIndex);
    }

    public void moveToNextQuestion() {
        currentQuestionIndex++;
        eliminatedOptions.clear();
        hint = null;
        message = null;
    }

    public int getUsedLifelinesCount() {
        return usedLifelines.size();
    }
}
