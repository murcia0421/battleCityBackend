package com.battlecity.battle_city_backend.controller;

import com.battlecity.model.GameRoom;
import com.battlecity.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private static final int MAX_PLAYERS = 4;
    private final Map<String, GameRoom> activeRooms = new ConcurrentHashMap<>();

    @MessageMapping("/room/{roomId}/players")
    @SendTo("/topic/room/{roomId}/players")
    public Player handlePlayerMessage(@DestinationVariable String roomId, Player player) {
        logger.info("Mensaje recibido para sala: {}", roomId);

        // Obtener o crear sala
        GameRoom room = activeRooms.computeIfAbsent(roomId,
                id -> new GameRoom(id, MAX_PLAYERS));

        String playerId = "Jugador " + (room.getPlayers().size() + 1);
        Player newPlayer = new Player(playerId, player.getName(), player.getTankColor());

        room.addPlayer(newPlayer);
        logger.info("Nuevo jugador añadido a la sala {} con ID: {}", roomId, playerId);

        return newPlayer;
    }

    @MessageMapping("/room/{roomId}/leave")
    @SendTo("/topic/room/{roomId}/players")
    public void handlePlayerLeave(@DestinationVariable String roomId, String playerId) {
        GameRoom room = activeRooms.get(roomId);
        if (room != null) {
            room.removePlayer(playerId);

            // Si la sala está vacía, la eliminamos completamente
            if (room.getPlayers().isEmpty()) {
                activeRooms.remove(roomId);
                logger.info("Sala {} eliminada por estar vacía", roomId);
            }
        }
        logger.info("Jugador {} eliminado de la sala {}", playerId, roomId);
    }

    @MessageMapping("/room/{roomId}/request-players")
    @SendTo("/topic/room/{roomId}/players")
    public List<Player> getPlayersStatus(@DestinationVariable String roomId) {
        GameRoom room = activeRooms.get(roomId);
        if (room != null) {
            logger.info("Solicitud de estado de jugadores para sala {}", roomId);
            return room.getPlayers();
        }
        return new ArrayList<>();  // Retornar lista vacía si la sala no existe
    }

    @MessageMapping("/room/{roomId}/leave-allplayers")
    @SendTo("/topic/room/{roomId}/players")
    public List<Player> handleAllPlayersLeave(@DestinationVariable String roomId) {
        logger.info("Eliminando todos los jugadores de la sala: {}", roomId);

        // Eliminar la sala completa
        activeRooms.remove(roomId);

        // Retornar lista vacía para actualizar a los clientes
        return new ArrayList<>();
    }
}