package com.battlecity.battle_city_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class GameController {

    // Mantener un registro del orden de los jugadores
    private final List<String> playerOrder = new ArrayList<>();

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
}