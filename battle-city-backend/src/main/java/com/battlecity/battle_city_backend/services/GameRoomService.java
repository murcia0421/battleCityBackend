package com.battlecity.battle_city_backend.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.battlecity.model.Position;
import com.battlecity.model.GameRoom;
import com.battlecity.model.Player;

@Service
public class GameRoomService {

    private Map<String, GameRoom> rooms = new HashMap<>();
    private static final int MAX_PLAYERS = 4;

    // Método para agregar un jugador a una sala
    public GameRoom addPlayerToRoom(String playerId, Position position, String direction) {
        // Buscar una sala disponible o crear una nueva
        GameRoom room = findAvailableRoom();

        // Si no se encuentra una sala, crea una nueva
        if (room == null) {
            room = new GameRoom();
            rooms.put(room.getRoomId(), room);
        }

        // Crear el jugador con la posición y dirección
        Player player = new Player();
        player.setId(playerId);
        player.setPosition(position);
        player.setDirection(direction);

        // Intentar agregar el jugador a la sala
        if (room.addPlayer(player)) {
            return room;
        } else {
            throw new RuntimeException("La sala está llena");
        }
    }

    // Método para encontrar una sala disponible
    private GameRoom findAvailableRoom() {
        for (GameRoom room : rooms.values()) {
            if (room.getPlayers().size() < MAX_PLAYERS) {
                return room;
            }
        }
        return null;
    }
}
