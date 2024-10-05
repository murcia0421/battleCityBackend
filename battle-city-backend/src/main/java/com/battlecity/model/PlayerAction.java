package com.battlecity.model;

public class PlayerAction {
    private String playerId;
    private String type;
    private String direction;

    // Getters y Setters
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "PlayerAction{" +
                "playerId='" + playerId + '\'' +
                ", type='" + type + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
