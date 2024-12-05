package com.battlecity.battle_city_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.battlecity.model.GameState;
import com.battlecity.battle_city_backend.services.GameService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private static final int INITIAL_LIVES = 3;
    private static final String PLAYER_ID = "playerId";  // Constant for playerId to avoid duplication
    private final List<String> playerOrder = new ArrayList<>();
    private final Map<String, Integer> playerLives = new ConcurrentHashMap<>();
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
        System.out.println(playerLives);

    }

    @MessageMapping("/update-walls")
    @SendTo("/topic/game-updates")
    public GameState handleWallUpdate() {
        return gameService.getCurrentGameState();
    }

    @MessageMapping("/game-start")
    @SendTo("/topic/game-updates")
    public Object handleGameStart(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode gameStartData = mapper.readTree(message);
            JsonNode playerInfo = gameStartData.get("player");
            String playerId = playerInfo.get(PLAYER_ID).asText();
            if (!playerOrder.contains(playerId)) {
                playerOrder.add(playerId);
            }
            return mapper.writeValueAsString(Map.of(
                    "type", "GAME_START",
                    "player", playerInfo,
                    "playerIndex", playerOrder.indexOf(playerId)
            ));
        } catch (Exception e) {
            logger.error("Error processing game start: {}", e.getMessage(), e);
            return message;
        }
    }

    // Endpoint para unirse
    @MessageMapping("/game-join")
    @SendTo("/topic/game-updates")
    public Object handleGameJoin(String joinMessage) {
        // Avoid logging sensitive user-controlled data
        logger.info("Player joining game");
        return joinMessage;
    }

    // Endpoint para movimiento
    @MessageMapping("/game-move")
    @SendTo("/topic/game-updates")
    public Object handleGameMove(String moveMessage) {
        // Avoid logging sensitive user-controlled data
        logger.info("Movement received");
        return moveMessage;
    }

    @MessageMapping("/bullet-fired")
    @SendTo("/topic/game-updates")
    public String handleBulletFired(String bulletMessage) {
        // Avoid logging sensitive user-controlled data
        logger.info("Bullet fired");
        return bulletMessage;
    }

    @MessageMapping("/bullet-update")
    @SendTo("/topic/game-updates")
    public String handleBulletUpdate(String updateMessage) {
        // Avoid logging sensitive user-controlled data
        logger.info("Bullet update received");
        return updateMessage;
    }



    @MessageMapping("/player-hit")
    @SendTo("/topic/game-updates")
    public String handlePlayerHit(String hitMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode hit = mapper.readTree(hitMessage);
            String playerId = hit.get("playerId").asText();

            // Reducir vidas
            int currentLives = playerLives.getOrDefault(playerId, INITIAL_LIVES);
            int newLives = Math.max(0, currentLives - 1);
            playerLives.put(playerId, newLives);

            // Si el jugador se quedó sin vidas
            if (newLives <= 0) {
                // Contar jugadores vivos
                long playersAlive = playerLives.values().stream()
                        .filter(lives -> lives > 0)
                        .count();
                System.out.println(playerLives);
                System.out.println(playersAlive);
                System.out.println(playerLives.getOrDefault(playerId, INITIAL_LIVES));

                // Si solo queda un jugador vivo
                if (playersAlive == 1) {
                    // Encontrar al ganador
                    String winner = playerLives.entrySet().stream()
                            .filter(entry -> entry.getValue() > 0)
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse(null);

                    return mapper.writeValueAsString(Map.of(
                            "type", "GAME_OVER",
                            "winner", winner
                    ));
                }

                return mapper.writeValueAsString(Map.of(
                        "type", "PLAYER_ELIMINATED",
                        "playerId", playerId
                ));
            }

            // Si aún tiene vidas
            return mapper.writeValueAsString(Map.of(
                    "type", "PLAYER_HIT",
                    "playerId", playerId,
                    "lives", newLives
            ));
        } catch (Exception e) {
            System.err.println("Error procesando hit: " + e.getMessage());
            return hitMessage;
        }

    }


}
