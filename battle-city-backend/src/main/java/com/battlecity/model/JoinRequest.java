package com.battlecity.model;

public class JoinRequest {

    private String playerId;
    private Position position;
    private String direction;

    // Constructor
    public JoinRequest(String playerId, Position position, String direction) {
        this.playerId = playerId;
        this.position = position;
        this.direction = direction;
    }

    // Getters y setters
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
