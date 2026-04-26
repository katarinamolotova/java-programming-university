package ru.hse.whowantstobeamillionaire.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.hse.whowantstobeamillionaire.model.Question;

@Repository
@RequiredArgsConstructor
public class QuestionRepository {
    private static final RowMapper<Question> QUESTION_ROW_MAPPER = (resultSet, rowNum) ->
            new Question(
                    resultSet.getString("question"),
                    List.of(
                            resultSet.getString("answer_1"),
                            resultSet.getString("answer_2"),
                            resultSet.getString("answer_3"),
                            resultSet.getString("answer_4")
                    ),
                    resultSet.getInt("correct_answer"),
                    resultSet.getInt("level")
            );

    private static final String SELECT_BY_LEVEL = """
            SELECT question, answer_1, answer_2, answer_3, answer_4, correct_answer, level
            FROM questions
            WHERE level = ?
            %s
            ORDER BY RANDOM()
            LIMIT 1""";

    private final JdbcTemplate jdbcTemplate;

    public List<Question> findRandomByLevel(int level, Collection<String> excludedQuestions) {
        List<Object> params = new ArrayList<>();
        params.add(level);

        StringBuilder excludedQuestionsBuilder = new StringBuilder();
        if (!excludedQuestions.isEmpty()) {
            excludedQuestionsBuilder.append(" AND question NOT IN (");
            excludedQuestionsBuilder.append("?, ".repeat(excludedQuestions.size()));
            excludedQuestionsBuilder.setLength(excludedQuestionsBuilder.length() - 2);
            excludedQuestionsBuilder.append(')');
            params.addAll(excludedQuestions);
        }

        String sql = String.format(SELECT_BY_LEVEL, excludedQuestionsBuilder);
        return jdbcTemplate.query(sql, QUESTION_ROW_MAPPER, params.toArray());
    }
}
