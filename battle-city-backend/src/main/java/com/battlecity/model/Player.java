package com.battlecity.model;

public class Player {
    private String id;
    private String name;  // Añadido campo name
    private Position position;
    private String direction;

    public Player() {
        // Constructor vacío necesario para deserialización
    }

    public Player(String id, String name) {
        this.id = id;
        this.name = name;  // Usar id como nombre por defecto
        this.position = null;
        this.direction = "down";
    }

    // Getters and Setters originales
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    // Nuevos getters y setters para name
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}