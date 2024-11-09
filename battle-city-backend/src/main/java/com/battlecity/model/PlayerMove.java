package com.battlecity.model;

public class PlayerMove {
    private String playerId;
    private String roomId; // Añadido roomId
    private int x;
    private int y;
    private String direction;

    // Constructor completo
    public PlayerMove(String playerId, String roomId, int x, int y, String direction) {
        this.playerId = playerId;
        this.roomId = roomId;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    // Getters y Setters
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
