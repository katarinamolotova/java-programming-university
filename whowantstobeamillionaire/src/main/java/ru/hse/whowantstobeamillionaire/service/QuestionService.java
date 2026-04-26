package ru.hse.whowantstobeamillionaire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.whowantstobeamillionaire.model.Question;
import ru.hse.whowantstobeamillionaire.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> getQuestionsForGame(int levelsCount) {
        List<Question> questions = new ArrayList<>();
        for (int level = 1; level <= levelsCount; level++) {
            int currentLevel = level;
            questions.add(getQuestionForLevel(currentLevel)
                    .orElseThrow(() -> new IllegalStateException("Отсутствуют вопросы для уровня " + currentLevel)));
        }
        return List.copyOf(questions);
    }

    public Optional<Question> getQuestionForLevel(int level) {
        return questionRepository.findRandomByLevel(level, List.of()).stream().findFirst();
    }

    public Optional<Question> getQuestionForLevelExcluding(int level, Collection<String> excludedQuestions) {
        return questionRepository.findRandomByLevel(level, excludedQuestions).stream().findFirst();
    }
}
