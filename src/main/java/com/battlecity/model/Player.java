package com.battlecity.model;


import com.fasterxml.jackson.annotation.JsonProperty;
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
    private int lives;
    private boolean alive;

    public Player() {
        // Constructor vacío necesario para deserialización
    }

    public Player(String id, String name, String tankColor, int lives, boolean isAlive) {
        this.id = id;
        this.name = name;
        this.tankColor = tankColor;
        this.position = null; // Posición inicial
        this.direction = "down"; // Dirección inicial
        this.lives = lives;
        this.alive = isAlive;
    }

    public Player(String playerId, String name, String tankColor) {
        this.id = playerId;
        this.name = name;
        this.tankColor = tankColor;
        this.position = null; // Posición inicial
        this.direction = "down"; // Dirección inicial
        this.alive = true;
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

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
    @JsonProperty("isAlive") // Este mapeo conecta el JSON con "isAlive" al atributo "alive".
    public boolean isAlive() {
        return alive;
    }

    @JsonProperty("isAlive")
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
