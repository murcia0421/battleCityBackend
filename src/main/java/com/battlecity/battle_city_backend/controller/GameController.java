package com.battlecity.battle_city_backend.controller;

import com.battlecity.model.Player;
import com.battlecity.model.Position;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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
    private static final String PLAYER_ID = "playerId";

    // Estructuras de datos organizadas por sala
    private final Map<String, List<String>> roomPlayerOrder = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Player>> roomPlayers = new ConcurrentHashMap<>();
    private final GameService gameService;

    public GameController(SimpMessagingTemplate messagingTemplate, GameService gameService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
    }

    @MessageMapping("/room/{roomId}/update-walls")
    @SendTo("/topic/room/{roomId}/game-updates")
    public GameState handleWallUpdate(@DestinationVariable String roomId) {
        return gameService.getCurrentGameState();
    }

    @MessageMapping("/room/{roomId}/game-start")
    @SendTo("/topic/room/{roomId}/game-updates")
    public Object handleGameStart(@DestinationVariable String roomId, String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode gameStartData = mapper.readTree(message);
            JsonNode playerInfo = gameStartData.get("player");
            String playerId = playerInfo.get(PLAYER_ID).asText();

            // Inicializar lista de orden para la sala si no existe
            roomPlayerOrder.putIfAbsent(roomId, new ArrayList<>());
            List<String> playerOrder = roomPlayerOrder.get(roomId);

            if (!playerOrder.contains(playerId)) {
                playerOrder.add(playerId);
            }

            logger.info("Game started in room {} for player: {} with index: {}",
                    roomId, playerId, playerOrder.indexOf(playerId));

            return mapper.writeValueAsString(Map.of(
                    "type", "GAME_START",
                    "player", playerInfo,
                    "playerIndex", playerOrder.indexOf(playerId)));
        } catch (Exception e) {
            logger.error("Error processing game start: {}", e.getMessage(), e);
            return message;
        }
    }

    @MessageMapping("/room/{roomId}/game-join")
    @SendTo("/topic/room/{roomId}/game-updates")
    public Object handleGameJoin(@DestinationVariable String roomId, String joinMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(joinMessage);
            JsonNode playerNode = jsonNode.get("player");

            Player player = mapper.treeToValue(playerNode, Player.class);
            String playerId = player.getId();

            // Inicializar mapa de jugadores para la sala si no existe
            roomPlayers.putIfAbsent(roomId, new ConcurrentHashMap<>());
            roomPlayers.get(roomId).put(playerId, player);

            logger.info("Player joined room {} - ID: {}, Name: {}, Lives: {}, Position: {}",
                    roomId, player.getId(), player.getName(), player.getLives(), player.getPosition());

            return joinMessage;
        } catch (Exception e) {
            logger.error("Error processing join message: {}", e.getMessage(), e);
            return joinMessage;
        }
    }

    @MessageMapping("/room/{roomId}/game-move")
    @SendTo("/topic/room/{roomId}/game-updates")
    public Object handleGameMove(@DestinationVariable String roomId, String moveMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode moveData = mapper.readTree(moveMessage);
            String playerId = moveData.get("playerId").asText();

            Map<String, Player> players = roomPlayers.get(roomId);
            if (players != null) {
                Player player = players.get(playerId);
                if (player != null) {
                    player.setPosition(mapper.treeToValue(moveData.get("position"), Position.class));
                    player.setDirection(moveData.get("direction").asText());
                    player.setLives(moveData.get("lives").asInt());
                    player.setAlive(moveData.get("isAlive").asBoolean());
                    player.setName(moveData.get("name").asText());

                    players.put(playerId, player);
                    logger.info("Player move in room {} - ID: {}, Position: ({}, {})",
                            roomId, playerId, player.getPosition().getX(), player.getPosition().getY());
                }
            }
            return moveMessage;
        } catch (Exception e) {
            logger.error("Error processing move message: {}", e.getMessage(), e);
            return moveMessage;
        }
    }

    @MessageMapping("/room/{roomId}/bullet-fired")
    @SendTo("/topic/room/{roomId}/game-updates")
    public String handleBulletFired(@DestinationVariable String roomId, String bulletMessage) {
        logger.info("Bullet fired in room {}", roomId);
        return bulletMessage;
    }

    @MessageMapping("/room/{roomId}/bullet-update")
    @SendTo("/topic/room/{roomId}/game-updates")
    public String handleBulletUpdate(@DestinationVariable String roomId, String updateMessage) {
        logger.info("Bullet update in room {}", roomId);
        return updateMessage;
    }

    @MessageMapping("/room/{roomId}/player-hit")
    public void handlePlayerHit(@DestinationVariable String roomId, String hitMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode hit = mapper.readTree(hitMessage);
            String playerId = hit.get("playerId").asText();

            Map<String, Player> players = roomPlayers.get(roomId);
            if (players == null) return;

            Player player = players.get(playerId);
            if (player == null) return;

            logger.info("Pre-hit in room {} - Player: {}, Lives: {}, IsAlive: {}",
                    roomId, player.getId(), player.getLives(), player.isAlive());

            int newLives = Math.max(0, player.getLives() - 1);
            player.setLives(newLives);

            if (newLives <= 0) {
                player.setAlive(false);
                players.put(playerId, player);

                messagingTemplate.convertAndSend("/topic/room/" + roomId + "/game-updates",
                        Map.of(
                                "type", "PLAYER_ELIMINATED",
                                "playerId", playerId
                        ));

                long playersAlive = players.values().stream()
                        .filter(Player::isAlive)
                        .count();

                if (playersAlive == 1) {
                    Player winner = players.values().stream()
                            .filter(Player::isAlive)
                            .findFirst()
                            .orElse(null);

                    if (winner != null) {
                        logger.info("Game over in room {}. Winner: {} ({})",
                                roomId, winner.getName(), winner.getId());

                        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/game-updates",
                                Map.of(
                                        "type", "GAME_OVER",
                                        "winner", winner.getId(),
                                        "nameWinner", winner.getName()
                                ));
                    }
                }
            } else {
                messagingTemplate.convertAndSend("/topic/room/" + roomId + "/game-updates",
                        Map.of(
                                "type", "PLAYER_HIT",
                                "playerId", playerId,
                                "lives", newLives
                        ));
            }
        } catch (Exception e) {
            logger.error("Error processing hit in room {}: {}", roomId, e.getMessage(), e);
        }
    }
}