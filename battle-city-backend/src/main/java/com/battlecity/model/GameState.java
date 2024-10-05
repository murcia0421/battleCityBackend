package com.battlecity.model;

import java.util.Map;

public class GameState {
    private Map<String, Player> players;

    // Getters y Setters
    public Map<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }
}
