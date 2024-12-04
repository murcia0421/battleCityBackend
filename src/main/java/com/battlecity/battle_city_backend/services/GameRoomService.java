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

    public GameRoomService() {
        rooms = new HashMap<>();
        initializeRooms();  // initialize rooms at the start
    }

    // Initialize rooms with predefined values
    public void initializeRooms() {
        rooms.put("room1", new GameRoom("room1", 2));
        rooms.put("room2", new GameRoom("room2", 2));
        rooms.put("room3", new GameRoom("room3", 2));
        rooms.put("room4", new GameRoom("room4", 2));
    }

    // Adds player to the specified room
    public boolean addPlayerToRoom(String roomId, Player player) {
        GameRoom gameRoom = rooms.get(roomId);
        if (gameRoom != null && gameRoom.canPlayer()) {
            gameRoom.addPlayer(player);
            return true;
        }
        return false;
    }

    // Retrieves the game room by its ID
    public GameRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }

    // Removes a player from the specified room
    public void removePlayerFromRoom(String roomId, Player player) {
        GameRoom gameRoom = rooms.get(roomId);
        if (gameRoom != null) {
            gameRoom.deletePlayer(player.getId());
        }
    }

    // Retrieves the list of players in the specified room
    public List<Player> getPlayersInRoom(String roomId) {
        GameRoom gameRoom = rooms.get(roomId);
        return gameRoom != null ? gameRoom.getPlayers() : null;
    }
}
