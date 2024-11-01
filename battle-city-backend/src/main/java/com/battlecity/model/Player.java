package com.battlecity.model;

public class Player {
    private String id;
    private Position position;
    private String direction;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
}