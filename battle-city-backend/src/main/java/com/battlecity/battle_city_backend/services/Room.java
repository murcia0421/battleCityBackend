// Room.java
package com.battlecity.battle_city_backend.services;

import java.util.HashSet;
import java.util.Set;

public class Room {
    private String id;
    private String name;
    private int maxPlayers;
    private Set<String> players = new HashSet<>();

    public Room(String id, String name, int maxPlayers) {
        this.id = id;
        this.name = name;
        this.maxPlayers = maxPlayers;
    }

    public boolean canJoin() {
        return players.size() < maxPlayers;
    }

    public void addPlayer(String playerName) {
        players.add(playerName);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Set<String> getPlayers() {
        return players;
    }
}
