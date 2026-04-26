package ru.hse.whowantstobeamillionaire.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hse.whowantstobeamillionaire.model.GameSession;
import ru.hse.whowantstobeamillionaire.model.LifelineType;
import ru.hse.whowantstobeamillionaire.model.PlayerRecord;
import ru.hse.whowantstobeamillionaire.model.PrizeRow;
import ru.hse.whowantstobeamillionaire.model.Question;
import ru.hse.whowantstobeamillionaire.service.GameService;

@Controller
@RequiredArgsConstructor
public class GameController {
    private static final String GAME_SESSION_KEY = "millionaire_game";
    private static final String DEFAULT_PLAYER_NAME = "Игрок";
    private static final int QUESTIONS_TO_WIN = 15;

    private final GameService gameService;

    @GetMapping("/")
    public String root() {
        return "redirect:/game";
    }

    @GetMapping("/game")
    public String game(Model model, HttpSession httpSession) {
        GameSession gameSession = getGame(httpSession);
        fillModel(model, gameSession);
        return "game";
    }

    @PostMapping("/game/new")
    public String newGame(
            @RequestParam(name = "playerName", required = false) String playerName,
            @RequestParam(name = "guaranteedAmount", required = false) Integer guaranteedAmount,
            HttpSession httpSession
    ) {
        httpSession.setAttribute(GAME_SESSION_KEY, gameService.startNewGame(playerName, guaranteedAmount));
        return "redirect:/game";
    }

    @PostMapping("/game/answer")
    public String answer(@RequestParam(name = "answer", required = false) Integer answer, HttpSession httpSession) {
        GameSession gameSession = getGame(httpSession);
        if (gameSession != null) {
            gameService.answer(gameSession, answer);
        }
        return "redirect:/game";
    }

    @PostMapping("/game/lifeline/{type}")
    public String lifeline(@PathVariable("type") String type, HttpSession httpSession) {
        GameSession gameSession = getGame(httpSession);
        if (gameSession == null) {
            return "redirect:/game";
        }
        try {
            LifelineType lifelineType = LifelineType.valueOf(type);
            gameService.applyLifeline(gameSession, lifelineType);
        } catch (IllegalArgumentException ignored) {
            gameSession.setMessage("Неизвестная подсказка.");
        }
        return "redirect:/game";
    }

    @PostMapping("/game/take-money")
    public String takeMoney(HttpSession httpSession) {
        GameSession gameSession = getGame(httpSession);
        if (gameSession != null) {
            gameService.takeMoney(gameSession);
        }
        return "redirect:/game";
    }

    private GameSession getGame(HttpSession httpSession) {
        Object rawSession = httpSession.getAttribute(GAME_SESSION_KEY);
        if (rawSession instanceof GameSession gameSession) {
            return gameSession;
        }
        return null;
    }

    private void fillModel(Model model, GameSession gameSession) {
        Question question = gameSession != null ? gameSession.getCurrentQuestion() : null;
        List<PlayerRecord> topRecords = gameService.getTopPlayerRecords();
        List<PrizeRow> prizeLadder = gameService.getPrizeLadder(gameSession);
        String playerNameValue = gameSession != null ? gameSession.getPlayerName() : DEFAULT_PLAYER_NAME;
        int selectedGuaranteedAmount = gameSession != null
                ? gameSession.getGuaranteedAmount()
                : gameService.getDefaultGuaranteedAmount();
        int questionNumber = gameSession == null
                ? 0
                : Math.min(gameSession.isFinished() ? gameSession.getCurrentQuestionIndex() : gameSession.getCurrentQuestionIndex() + 1, QUESTIONS_TO_WIN);

        model.addAttribute("game", gameSession);
        model.addAttribute("question", question);
        model.addAttribute("lifelines", LifelineType.values());
        model.addAttribute("optionIndexes", List.of(0, 1, 2, 3));
        model.addAttribute("prizeRows", prizeLadder);
        model.addAttribute("topRecords", topRecords);
        model.addAttribute("playerNameValue", playerNameValue);
        model.addAttribute("selectedGuaranteedAmount", selectedGuaranteedAmount);
        model.addAttribute("availableGuaranteedAmounts", gameService.getAvailableGuaranteedAmounts());
        model.addAttribute("questionNumber", questionNumber);
        model.addAttribute("currentAmount", gameService.getCurrentAmount(gameSession));
    }
}
