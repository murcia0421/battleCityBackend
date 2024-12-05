package com.battlecity.model;

public class PlayerDTO {

    private String id;
    private String name;
    private int score;
    private Position position;
    private String direction;
    private String tankColor;

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

    public String getTankColor() {
        return tankColor;
    }

    public void setTankColor(String tankColor) {
        this.tankColor = tankColor;
    }




    // Getters y setters
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
