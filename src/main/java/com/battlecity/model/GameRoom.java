package com.battlecity.model;

import java.util.*;

public class GameRoom {

    private String roomId;
    private List<Player> players;
    private int maxPlayers;

    // Constructor to initialize the game room with an ID and maximum number of players
    public GameRoom(String roomId, int maxPlayers) {
        this.roomId = roomId;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
    }

    // Checks if there is space for another player in the room
    public boolean canPlayerJoin() {
        return players.size() < this.maxPlayers;
    }

    // Adds a player to the game room
    public void addPlayer(Player player) {
        if (this.canPlayerJoin()) {
            players.add(player);
        }
    }

    // Removes a player by their ID from the game room
    public void removePlayer(String playerId) {
        players.removeIf(player -> player.getId().equals(playerId));
    }

    // Getters and setters
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
