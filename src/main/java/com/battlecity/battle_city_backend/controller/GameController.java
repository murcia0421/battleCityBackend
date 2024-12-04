package com.battlecity.battle_city_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.battlecity.model.GameState;
import com.battlecity.battle_city_backend.services.GameService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Controller
public class GameController {

    //Vidas jugador
    private static final int INITIAL_LIVES = 3;
    // Mantener un registro del orden de los jugadores
    private final List<String> playerOrder = new ArrayList<>();
    //Jugadores
    private final Map<String, Integer> playerLives = new ConcurrentHashMap<>();

    private final GameService gameService;
    public GameController(GameService gameService) {
        this.gameService = gameService;
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
            // Convertir el mensaje a objeto para procesarlo
            ObjectMapper mapper = new ObjectMapper();
            JsonNode gameStartData = mapper.readTree(message);

            // Extraer la información del jugador
            JsonNode playerInfo = gameStartData.get("player");
            String playerId = playerInfo.get("id").asText();

            // Si es el primer jugador en iniciar, agregar a la lista
            if (!playerOrder.contains(playerId)) {
                playerOrder.add(playerId);
            }

            // Devolver el mensaje con información adicional del orden
            return mapper.writeValueAsString(Map.of(
                    "type", "GAME_START",
                    "player", playerInfo,
                    "playerIndex", playerOrder.indexOf(playerId)
            ));
        } catch (Exception e) {
            System.err.println("Error procesando inicio de juego: " + e.getMessage());
            return message;
        }
    }

    // Endpoint para unirse
    @MessageMapping("/game-join")
    @SendTo("/topic/game-updates")
    public Object handleGameJoin(String joinMessage) {
        System.out.println("Jugador uniéndose: " + joinMessage);
        return joinMessage;
    }

    // Endpoint para movimiento
    @MessageMapping("/game-move")
    @SendTo("/topic/game-updates")
    public Object handleGameMove(String moveMessage) {
        System.out.println("Movimiento recibido: " + moveMessage);
        return moveMessage;
    }

    @MessageMapping("/bullet-fired")
    @SendTo("/topic/game-updates")
    public String handleBulletFired(String bulletMessage) {
        System.out.println("Disparo recibido: " + bulletMessage);
        return bulletMessage;
    }

    @MessageMapping("/bullet-update")
    @SendTo("/topic/game-updates")
    public String handleBulletUpdate(String updateMessage) {
        System.out.println("Actualización de bala: " + updateMessage);
        return updateMessage;
    }

    @MessageMapping("/player-hit")
    @SendTo("/topic/game-updates")
    public String handlePlayerHit(String hitMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode hit = mapper.readTree(hitMessage);
            String playerId = hit.get("playerId").asText();

            // Reducir vidas del jugador impactado
            int lives = playerLives.getOrDefault(playerId, INITIAL_LIVES) - 1;
            playerLives.put(playerId, lives);

            // Si el jugador perdió todas sus vidas
            if (lives <= 0) {
                return mapper.writeValueAsString(Map.of(
                        "type", "PLAYER_ELIMINATED",
                        "playerId", playerId
                ));
            }

            // Si aún tiene vidas
            return mapper.writeValueAsString(Map.of(
                    "type", "PLAYER_HIT",
                    "playerId", playerId,
                    "lives", lives
            ));

        } catch (Exception e) {
            System.err.println("Error procesando hit: " + e.getMessage());
            return hitMessage;
        }
    }

    @MessageMapping("/player-respawn")
    @SendTo("/topic/game-updates")
    public String handlePlayerRespawn(String respawnMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode respawn = mapper.readTree(respawnMessage);
            String playerId = respawn.get("playerId").asText();

            // Reiniciar vidas del jugador
            playerLives.put(playerId, INITIAL_LIVES);

            // Calcular posición de respawn basada en el índice del jugador
            int playerIndex = playerOrder.indexOf(playerId);
            Map<String, Object> respawnPosition = playerIndex == 0 ?
                    Map.of("x", 1, "y", 1) :
                    Map.of("x", 2, "y", 9);

            return mapper.writeValueAsString(Map.of(
                    "type", "PLAYER_RESPAWN",
                    "playerId", playerId,
                    "lives", INITIAL_LIVES,
                    "position", respawnPosition
            ));

        } catch (Exception e) {
            System.err.println("Error procesando respawn: " + e.getMessage());
            return respawnMessage;
        }
    }
}
