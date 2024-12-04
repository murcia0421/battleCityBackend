package com.battlecity.model;

public class Wall {
    private String id;
    private Position position;
    private boolean isDestroyed;

    public Wall(String id, Position position) {
        this.id = id;
        this.position = position;
        this.isDestroyed = false;
    }

    public String getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void destroy() {
        this.isDestroyed = true;
    }
}
