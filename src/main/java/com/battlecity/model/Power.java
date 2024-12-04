package com.battlecity.model;

public class Power {

    private int row;
    private int col;
    private String type; // Tipo de poder (ejemplo: "speed", "shield", "extra-life")

    public Power(int row, int col, String type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    // Getters y setters
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getType() {
        return type;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setType(String type) {
        this.type = type;
    }

}
