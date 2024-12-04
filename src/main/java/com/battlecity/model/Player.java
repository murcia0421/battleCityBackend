package com.battlecity.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "Players")
@Data
public class Player {
    @Id
    private String id;
    private String name; // Añadido campo name
    private Position position;
    private String direction;
    private String tankColor;

    public Player() {
        // Constructor vacío necesario para deserialización
    }

    public Player(String id, String name, String tankColor) {
        this.id = id;
        this.name = name;
        this.tankColor = tankColor;
        this.position = null; // Posición inicial
        this.direction = "down"; // Dirección inicial
    }

    // Getters and Setters originales
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    // Nuevos getters y setters para name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTankColor() {
        return tankColor;
    }

    public void setTankColor(String tankColor) {
        this.tankColor = tankColor;
    }
}