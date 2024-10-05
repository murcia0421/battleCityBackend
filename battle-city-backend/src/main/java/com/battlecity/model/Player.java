package com.battlecity.model;

public class Player {
    private String id;
    private String name;
    private int x; // Posición en el eje X
    private int y; // Posición en el eje Y
    private String direction; // Dirección del jugador

    // Constructor
    public Player(String id, int x, int y, String direction) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
