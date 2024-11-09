package com.battlecity.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameRoom {
    private String roomId;
    private List<Player> players;

    public GameRoom() {
        this.roomId = UUID.randomUUID().toString(); // Generar un ID único para la sala
        this.players = new ArrayList<>();
    }

    public String getRoomId() {
        return roomId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public boolean addPlayer(Player player) {
        if (players.size() < 4) { // Verifica que no haya más de 4 jugadores
            players.add(player);
            return true;
        }
        return false;
    }
}
