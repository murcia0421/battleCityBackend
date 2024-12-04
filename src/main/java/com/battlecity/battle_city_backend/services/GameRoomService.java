package com.battlecity.battle_city_backend.services;

import com.battlecity.model.GameRoom;
import com.battlecity.model.Player;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameRoomService {

    private Map<String, GameRoom> rooms;

    // Constructor to initialize rooms map
    public GameRoomService() {
        rooms = new HashMap<>();
    }

    // Method to initialize rooms with sample data
    public void initialiceRoom() {
        rooms.put("room1", new GameRoom("room1", 2));
        rooms.put("room2", new GameRoom("room2", 2));
        rooms.put("room3", new GameRoom("room3", 2));
        rooms.put("room4", new GameRoom("room4", 2));
    }

    // Add player to the room if there's space
    public boolean addPlayerToRoom(String roomId, Player player) {
        GameRoom gameRoom = rooms.get(roomId);
        if (gameRoom != null && gameRoom.canPlayerJoin()) {
            gameRoom.addPlayer(player);
            return true;
        }
        return false;
    }

    // Get the room by its ID
    public GameRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }

    // Remove a player from a room
    public void removePlayerFromRoom(String roomId, Player player) {
        GameRoom gameRoom = rooms.get(roomId);
        if (gameRoom != null) {
            gameRoom.removePlayer(player.getId());
        }
    }

    // Get all players in a specific room
    public List<Player> getPlayersInRoom(String roomId) {
        GameRoom gameRoom = rooms.get(roomId);
        return gameRoom != null ? gameRoom.getPlayers() : null;
    }
}
