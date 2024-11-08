// GameState.java
package com.battlecity.model;

import java.util.Map;
import java.util.List;

public class GameState {
    private String type;
    private String playerId;
    private Map<String, Player> players;
    private List<Bullet> bullets;

    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public Map<String, Player> getPlayers() { return players; }
    public void setPlayers(Map<String, Player> players) { this.players = players; }

    public List<Bullet> getBullets() { return bullets; }
    public void setBullets(List<Bullet> bullets) {
        this.bullets = null;
    }
}