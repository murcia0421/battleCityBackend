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
    private static final int INITIAL_LIVES = 3;
    private static final String PLAYER_ID = "playerId";  // Constant for playerId to avoid duplication
    private final List<String> playerOrder = new ArrayList<>();
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    private final Map<String, Integer> playerLives = new ConcurrentHashMap<>();
    private final GameService gameService;

    public GameController(SimpMessagingTemplate messagingTemplate, GameService gameService) {
        this.messagingTemplate = messagingTemplate;
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
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(joinMessage);
            JsonNode playerNode = jsonNode.get("player");

            Player player = mapper.treeToValue(playerNode, Player.class);
            String playerId = player.getId();
            System.out.println("player :" + player.getId() + "nombre :" + player.getName() +
                    "numero de vidas: " + player.isAlive() + player.getLives() + "posicion: " +player.getPosition());
            players.put(playerId, player);

            logger.info("Player joining game with ID: " + playerId);


            return joinMessage; // Mantenemos el mismo formato de respuesta

        } catch (Exception e) {
            logger.error("Error processing join message: " + e.getMessage());
            return joinMessage; // En caso de error, devolvemos el mensaje original
        }
    }

    // Método para obtener un jugador (por si lo necesitas)
    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    // Endpoint para movimiento
    @MessageMapping("/game-move")
    @SendTo("/topic/game-updates")
    public Object handleGameMove(String moveMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode moveData = mapper.readTree(moveMessage);

            String playerId = moveData.get("playerId").asText();
            Player player = players.get(playerId);

            if (player != null) {
                // Actualizar los datos del jugador
                player.setPosition(mapper.treeToValue(moveData.get("position"), Position.class));
                player.setDirection(moveData.get("direction").asText());
                player.setLives(moveData.get("lives").asInt());
                player.setAlive(moveData.get("isAlive").asBoolean());
                player.setName(moveData.get("name").asText());

                // Guardar el jugador actualizado
                players.put(playerId, player);

                logger.info("Player " + playerId + " position updated");
            }
            System.out.println("player :" + player.getId() +" "+ "nombre :" + player.getName() +" "+
                    "numero de vidas: " + player.isAlive() +" " + player.getLives()
                    + "posicionX: " + player.getPosition().getX() +" "+ "posicionY: " + player.getPosition().getY());
            players.put(playerId, player);

            return moveMessage; // Mantenemos el formato de respuesta original

        } catch (Exception e) {
            logger.error("Error processing move message: " + e.getMessage());
            return moveMessage;
        }
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
    public void handlePlayerHit(String hitMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode hit = mapper.readTree(hitMessage);
            String playerId = hit.get("playerId").asText();

            // Obtener el jugador del HashMap
            Player player = players.get(playerId);
            System.out.println("Pre-hit - Player:" + player.getId() +
                    " Lives:" + player.getLives() +
                    " IsAlive:" + player.isAlive());

            // Reducir vidas
            int newLives = Math.max(0, player.getLives() - 1);
            player.setLives(newLives);

            if (newLives <= 0) {
                player.setAlive(false);
                players.put(playerId, player);

                // Enviamos primero el mensaje de eliminación
                messagingTemplate.convertAndSend("/topic/game-updates",
                        Map.of(
                                "type", "PLAYER_ELIMINATED",
                                "playerId", playerId
                        )
                );

                // Verificamos si hay un ganador
                long playersAlive = players.values().stream()
                        .filter(Player::isAlive)
                        .count();

                if (playersAlive == 1) {
                    String winner = players.values().stream()
                            .filter(Player::isAlive)
                            .map(Player::getId)
                            .findFirst()
                            .orElse(null);

                    // Enviamos el mensaje de victoria
                    messagingTemplate.convertAndSend("/topic/game-updates",
                            Map.of(
                                    "type", "GAME_OVER",
                                    "winner", winner
                            )
                    );
                }
            } else {
                // Si aún tiene vidas
                messagingTemplate.convertAndSend("/topic/game-updates",
                        Map.of(
                                "type", "PLAYER_HIT",
                                "playerId", playerId,
                                "lives", newLives
                        )
                );
            }

        } catch (Exception e) {
            logger.error("Error procesando hit: " + e.getMessage());
        }
    }
}
