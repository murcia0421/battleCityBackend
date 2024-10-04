package com.battlecity.model;

import java.util.HashMap;
import java.util.Map;

public class GameState {

    private Map<String, Player> players = new HashMap<>();
    private Map<String, Object> gameObjects = new HashMap<>(); // Paredes, árboles, etc.

    public Map<String, Player> getPlayers() {
        return players;
    }

    public void addPlayer(String playerId, Player player) {
        players.put(playerId, player);
    }

    public Map<String, Object> getGameObjects() {
        return gameObjects;
    }
}
