package ru.hse.whowantstobeamillionaire.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.whowantstobeamillionaire.model.GameSession;
import ru.hse.whowantstobeamillionaire.model.LifelineType;
import ru.hse.whowantstobeamillionaire.model.PlayerRecord;
import ru.hse.whowantstobeamillionaire.model.PrizeRow;
import ru.hse.whowantstobeamillionaire.model.Question;

@Service
@RequiredArgsConstructor
public class GameService {
    private static final int QUESTIONS_TO_WIN = 15;
    private static final int MAX_LIFELINES_USAGE = 4;
    private static final List<Integer> PRIZE_LADDER = List.of(
            500, 1_000, 2_000, 3_000, 5_000,
            10_000, 15_000, 25_000, 50_000, 100_000,
            200_000, 400_000, 800_000, 1_500_000, 3_000_000
    );

    private final Random random = new SecureRandom();
    private final QuestionService questionService;
    private final PlayerRecordService playerRecordService;

    public GameSession startNewGame(String playerName, Integer guaranteedAmount) {
        List<Question> selected = selectQuestions();
        return new GameSession(
                selected,
                playerRecordService.normalizePlayerName(playerName),
                normalizeGuaranteedAmount(guaranteedAmount)
        );
    }

    public List<Integer> getAvailableGuaranteedAmounts() {
        return new ArrayList<>(PRIZE_LADDER);
    }

    public int getDefaultGuaranteedAmount() {
        return PRIZE_LADDER.get(0);
    }

    public int getCurrentAmount(GameSession session) {
        if (session == null) {
            return 0;
        }
        if (session.isFinished()) {
            return session.getWonAmount();
        }
        return currentEarnedAmount(session);
    }

    public void answer(GameSession session, Integer selectedOptionIndex) {
        if (session == null || session.isFinished()) {
            return;
        }

        Question question = session.getCurrentQuestion();
        if (question == null) {
            finishGame(session, true, false, PRIZE_LADDER.get(PRIZE_LADDER.size() - 1), session.getMessage());
            return;
        }

        if (selectedOptionIndex == null || selectedOptionIndex < 0 || selectedOptionIndex >= question.options().size()) {
            session.setMessage("Выберите один из доступных вариантов ответа.");
            return;
        }

        if (Objects.equals(selectedOptionIndex, getCorrectOptionIndex(question))) {
            session.setMessage("Верный ответ.");
            advance(session);
            return;
        }

        if (session.isRightToErrorActive()) {
            session.setRightToErrorActive(false);
            session.setMessage("Ответ неверный, но подсказка «Право на ошибку» сохранила игру.");
            advance(session);
            return;
        }

        finishGame(session, false, false, safePayoutAmount(session), "Неверный ответ. Игра окончена.");
    }

    public void takeMoney(GameSession session) {
        if (session == null || session.isFinished()) {
            return;
        }
        int amount = currentEarnedAmount(session);
        finishGame(session, false, true, amount, "Вы забрали " + amount + ".");
    }

    public List<PlayerRecord> getTopPlayerRecords() {
        return playerRecordService.getTopRecords();
    }

    public List<PrizeRow> getPrizeLadder(GameSession gameSession) {
        int currentLevel = 0;
        if (gameSession != null && !gameSession.isFinished()) {
            currentLevel = Math.min(gameSession.getCurrentQuestionIndex() + 1, PRIZE_LADDER.size());
        }
        List<PrizeRow> rows = new ArrayList<>();
        for (int level = PRIZE_LADDER.size(); level >= 1; level--) {
            int amount = PRIZE_LADDER.get(level - 1);
            PrizeRow prizeRow = new PrizeRow(level, amount, level == currentLevel);
            rows.add(prizeRow);
        }
        return rows;
    }

    public void applyLifeline(GameSession session, LifelineType lifelineType) {
        if (session == null || session.isFinished()) {
            return;
        }

        if (session.getUsedLifelines().contains(lifelineType)) {
            session.setMessage("Эта подсказка уже была использована.");
            return;
        }

        if (session.getUsedLifelinesCount() >= MAX_LIFELINES_USAGE) {
            session.setMessage("За одну игру можно использовать не более четырёх подсказок.");
            return;
        }

        Question current = session.getCurrentQuestion();
        if (current == null) {
            return;
        }

        boolean lifelineApplied = true;
        switch (lifelineType) {
            case AUDIENCE_HELP -> applyAudienceHelp(session, current);
            case FIFTY_FIFTY -> applyFiftyFifty(session, current);
            case PHONE_FRIEND -> applyPhoneFriend(session, current);
            case RIGHT_TO_ERROR -> {
                session.setRightToErrorActive(true);
                session.setHint("Подсказка «Право на ошибку» активна на один неверный ответ.");
            }
            case REPLACE_QUESTION -> lifelineApplied = replaceQuestion(session, current);
        }

        if (!lifelineApplied) {
            return;
        }

        session.getUsedLifelines().add(lifelineType);
        session.setMessage("Подсказка использована: " + lifelineType.getName());
    }

    private void advance(GameSession session) {
        session.moveToNextQuestion();
        if (session.getCurrentQuestionIndex() >= QUESTIONS_TO_WIN) {
            finishGame(session, true, false, PRIZE_LADDER.get(PRIZE_LADDER.size() - 1), "Поздравляем! Вы выиграли игру.");
        }
    }

    private void finishGame(GameSession session, boolean won, boolean tookMoney, int amount, String message) {
        session.setFinished(true);
        session.setWon(won);
        session.setTookMoney(tookMoney);
        session.setWonAmount(amount);
        session.setMessage(message);
        saveRecordIfNeeded(session);
    }

    private void saveRecordIfNeeded(GameSession session) {
        if (session.isRecordSaved()) {
            return;
        }

        playerRecordService.saveRecord(
                session.getPlayerName(),
                session.getWonAmount(),
                Math.min(session.getCurrentQuestionIndex(), QUESTIONS_TO_WIN)
        );
        session.setRecordSaved(true);
    }

    private int currentEarnedAmount(GameSession session) {
        int answered = session.getCurrentQuestionIndex();
        if (answered <= 0) {
            return 0;
        }
        int idx = Math.min(answered - 1, PRIZE_LADDER.size() - 1);
        return PRIZE_LADDER.get(idx);
    }

    private int safePayoutAmount(GameSession session) {
        int earnedAmount = currentEarnedAmount(session);
        if (earnedAmount < session.getGuaranteedAmount()) {
            return 0;
        }
        return session.getGuaranteedAmount();
    }

    private int normalizeGuaranteedAmount(Integer guaranteedAmount) {
        if (guaranteedAmount != null && PRIZE_LADDER.contains(guaranteedAmount)) {
            return guaranteedAmount;
        }
        return getDefaultGuaranteedAmount();
    }

    private int getCorrectOptionIndex(Question question) {
        int correctOptionIndex = question.correctOptionIndex() - 1;
        if (correctOptionIndex < 0 || correctOptionIndex >= question.options().size()) {
            throw new IllegalStateException("Некорректный индекс правильного ответа для вопроса: " + question.text());
        }
        return correctOptionIndex;
    }

    private void applyFiftyFifty(GameSession session, Question current) {
        session.getEliminatedOptions().clear();
        int correctOptionIndex = getCorrectOptionIndex(current);
        List<Integer> wrong = new ArrayList<>();
        for (int i = 0; i < current.options().size(); i++) {
            if (i != correctOptionIndex) {
                wrong.add(i);
            }
        }

        int keepWrong = wrong.get(random.nextInt(wrong.size()));
        for (Integer wrongOption : wrong) {
            if (!wrongOption.equals(keepWrong)) {
                session.getEliminatedOptions().add(wrongOption);
            }
        }
        session.setHint("Два неверных варианта убраны.");
    }

    private void applyAudienceHelp(GameSession session, Question current) {
        int correct = getCorrectOptionIndex(current);
        int correctPct = 45 + random.nextInt(31);
        int left = 100 - correctPct;
        int first = left / 3;
        int second = (left - first) / 2;
        int third = left - first - second;

        int[] percentages = new int[4];
        percentages[correct] = correctPct;
        List<Integer> wrongIndexes = Stream.of(0, 1, 2, 3)
                .filter(i -> i != correct)
                .toList();
        percentages[wrongIndexes.get(0)] = first;
        percentages[wrongIndexes.get(1)] = second;
        percentages[wrongIndexes.get(2)] = third;

        session.setHint(String.format(
                "Помощь зала: «%s» — %d%%, «%s» — %d%%, «%s» — %d%%, «%s» — %d%%",
                current.options().get(0), percentages[0],
                current.options().get(1), percentages[1],
                current.options().get(2), percentages[2],
                current.options().get(3), percentages[3]
        ));
    }

    private void applyPhoneFriend(GameSession session, Question current) {
        boolean suggestsCorrect = random.nextInt(100) < 80;
        int correctOptionIndex = getCorrectOptionIndex(current);
        int suggestedIndex;
        if (suggestsCorrect) {
            suggestedIndex = correctOptionIndex;
        } else {
            List<Integer> wrong = new ArrayList<>();
            for (int i = 0; i < current.options().size(); i++) {
                if (i != correctOptionIndex) {
                    wrong.add(i);
                }
            }
            suggestedIndex = wrong.get(random.nextInt(wrong.size()));
        }
        session.setHint("Друг советует ответ: «" + current.options().get(suggestedIndex) + "».");
    }

    private boolean replaceQuestion(GameSession session, Question current) {
        return questionService.getQuestionForLevelExcluding(current.level(), List.of(current.text()))
                .map(replacement -> {
                    session.getQuestions().set(session.getCurrentQuestionIndex(), replacement);
                    session.getEliminatedOptions().clear();
                    session.setHint("Текущий вопрос заменён.");
                    return true;
                })
                .orElseGet(() -> {
                    session.setHint("Для этого уровня нет другого вопроса.");
                    return false;
                });
    }

    private List<Question> selectQuestions() {
        return questionService.getQuestionsForGame(QUESTIONS_TO_WIN);
    }
}
