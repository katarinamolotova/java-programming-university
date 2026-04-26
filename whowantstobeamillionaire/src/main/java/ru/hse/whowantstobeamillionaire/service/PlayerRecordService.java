package ru.hse.whowantstobeamillionaire.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.whowantstobeamillionaire.model.PlayerRecord;
import ru.hse.whowantstobeamillionaire.repository.PlayerRecordRepository;

@Service
@RequiredArgsConstructor
public class PlayerRecordService {
    private static final int TOP_RECORDS_LIMIT = 10;
    private static final int MAX_PLAYER_NAME_LENGTH = 50;
    private static final String DEFAULT_PLAYER_NAME = "Игрок";

    private final PlayerRecordRepository playerRecordRepository;

    public void saveRecord(String playerName, int amount, int achievedLevel) {
        PlayerRecord playerRecord = new PlayerRecord(normalizePlayerName(playerName), amount, achievedLevel);
        playerRecordRepository.save(playerRecord);
    }

    public List<PlayerRecord> getTopRecords() {
        return playerRecordRepository.findTopRecords(TOP_RECORDS_LIMIT);
    }

    public String normalizePlayerName(String playerName) {
        if (playerName == null) {
            return DEFAULT_PLAYER_NAME;
        }

        String normalized = playerName.trim();
        if (normalized.isEmpty()) {
            return DEFAULT_PLAYER_NAME;
        }

        if (normalized.length() > MAX_PLAYER_NAME_LENGTH) {
            return normalized.substring(0, MAX_PLAYER_NAME_LENGTH);
        }

        return normalized;
    }
}
