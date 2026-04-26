package ru.hse.whowantstobeamillionaire.repository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.hse.whowantstobeamillionaire.model.PlayerRecord;

@Repository
@RequiredArgsConstructor
public class PlayerRecordRepository {
    private static final RowMapper<PlayerRecord> PLAYER_RECORD_ROW_MAPPER = (resultSet, rowNum) -> new PlayerRecord(
            resultSet.getString("player_name"),
            resultSet.getInt("amount"),
            resultSet.getInt("achieved_level")
    );

    private static final String INSERT_RECORD = """
            INSERT INTO player_records (player_name, amount, achieved_level)
            VALUES (?, ?, ?)
            """;

    private static final String SELECT_TOP_RECORDS = """
            SELECT player_name, amount, achieved_level
            FROM player_records
            ORDER BY amount DESC, achieved_level DESC, id ASC
            LIMIT ?
            """;

    private final JdbcTemplate jdbcTemplate;

    public void save(PlayerRecord playerRecord) {
        jdbcTemplate.update(
                INSERT_RECORD,
                playerRecord.playerName(),
                playerRecord.amount(),
                playerRecord.achievedLevel()
        );
    }

    public List<PlayerRecord> findTopRecords(int limit) {
        return jdbcTemplate.query(SELECT_TOP_RECORDS, PLAYER_RECORD_ROW_MAPPER, limit);
    }
}
