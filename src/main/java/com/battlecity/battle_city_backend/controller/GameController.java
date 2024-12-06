package com.battlecity.battle_city_backend.controller;

import com.battlecity.model.Player;
import com.battlecity.model.Position;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.battlecity.model.GameState;
import com.battlecity.battle_city_backend.services.GameService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class GameController {
    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    // private static final int INITIAL_LIVES = 3;
    private static final String PLAYER_ID = "playerId"; // Constant for playerId to avoid duplication
    private final List<String> playerOrder = new ArrayList<>();
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    private final Map<String, Integer> playerLives = new ConcurrentHashMap<>();
    private final GameService gameService;

    public GameController(SimpMessagingTemplate messagingTemplate, GameService gameService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
        logger.info("Initialized GameController with empty playerLives map: {}", playerLives);
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
            logger.info("Game started for player: {} with index: {}", playerId, playerOrder.indexOf(playerId));
            return mapper.writeValueAsString(Map.of(
                    "type", "GAME_START",
                    "player", playerInfo,
                    "playerIndex", playerOrder.indexOf(playerId)));
        } catch (Exception e) {
            logger.error("Error processing game start: {}", e.getMessage(), e);
            return message;
        }
    }

    @MessageMapping("/game-join")
    @SendTo("/topic/game-updates")
    public Object handleGameJoin(String joinMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(joinMessage);
            JsonNode playerNode = jsonNode.get("player");

            Player player = mapper.treeToValue(playerNode, Player.class);
            String playerId = player.getId();

            players.put(playerId, player);

            logger.info("Player joined - ID: {}, Name: {}, Lives: {}, Position: {}",
                    player.getId(), player.getName(), player.getLives(), player.getPosition());
            return joinMessage;

        } catch (Exception e) {
            logger.error("Error processing join message: {}", e.getMessage(), e);
            return joinMessage;
        }
    }

    @MessageMapping("/game-move")
    @SendTo("/topic/game-updates")
    public Object handleGameMove(String moveMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode moveData = mapper.readTree(moveMessage);

            String playerId = moveData.get("playerId").asText();
            Player player = players.get(playerId);

            if (player != null) {
                player.setPosition(mapper.treeToValue(moveData.get("position"), Position.class));
                player.setDirection(moveData.get("direction").asText());
                player.setLives(moveData.get("lives").asInt());
                player.setAlive(moveData.get("isAlive").asBoolean());
                player.setName(moveData.get("name").asText());

                players.put(playerId, player);

                logger.info("Player move - ID: {}, Name: {}, Lives: {}, Position: ({}, {})",
                        player.getId(), player.getName(), player.getLives(),
                        player.getPosition().getX(), player.getPosition().getY());
            }

            return moveMessage;

        } catch (Exception e) {
            logger.error("Error processing move message: {}", e.getMessage(), e);
            return moveMessage;
        }
    }

    @MessageMapping("/bullet-fired")
    @SendTo("/topic/game-updates")
    public String handleBulletFired(String bulletMessage) {
        logger.info("Bullet fired event received");
        return bulletMessage;
    }

    @MessageMapping("/bullet-update")
    @SendTo("/topic/game-updates")
    public String handleBulletUpdate(String updateMessage) {
        logger.info("Bullet update event received");
        return updateMessage;
    }

    @MessageMapping("/player-hit")
    public void handlePlayerHit(String hitMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode hit = mapper.readTree(hitMessage);
            String playerId = hit.get("playerId").asText();

            Player player = players.get(playerId);
            logger.info("Pre-hit - Player: {}, Lives: {}, IsAlive: {}",
                    player.getId(), player.getLives(), player.isAlive());

            int newLives = Math.max(0, player.getLives() - 1);
            player.setLives(newLives);

            if (newLives <= 0) {
                player.setAlive(false);
                players.put(playerId, player);

                messagingTemplate.convertAndSend("/topic/game-updates",
                        Map.of(
                                "type", "PLAYER_ELIMINATED",
                                "playerId", playerId));

                long playersAlive = players.values().stream()
                        .filter(Player::isAlive)
                        .count();

                if (playersAlive == 1) {
                    String winner = players.values().stream()
                            .filter(Player::isAlive)
                            .map(Player::getId)
                            .findFirst()
                            .orElse(null);

                    messagingTemplate.convertAndSend("/topic/game-updates",
                            Map.of(
                                    "type", "GAME_OVER",
                                    "winner", winner));
                }
            } else {
                messagingTemplate.convertAndSend("/topic/game-updates",
                        Map.of(
                                "type", "PLAYER_HIT",
                                "playerId", playerId,
                                "lives", newLives));
            }
        } catch (Exception e) {
            logger.error("Error processing hit: {}", e.getMessage(), e);
        }
    }
}
