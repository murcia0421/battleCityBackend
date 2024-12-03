package com.battlecity.model;

public class PlayerAction {
    private String type;
    private String playerId;
    private Position position;
    private String direction;
    private Bullet bullet;

    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public Bullet getBullet() { return bullet; }
    public void setBullet(Bullet bullet) { this.bullet = bullet; }
}
