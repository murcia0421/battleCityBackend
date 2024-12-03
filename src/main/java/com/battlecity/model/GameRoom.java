package com.battlecity.model;

import java.util.*;

public class GameRoom {

    private String roomId;
    private List<Player> players;

    private int maxPlayer;

    public GameRoom(String roomId, int maxPlayer) {
        this.maxPlayer = maxPlayer;
        this.roomId = roomId;
        this.players = new ArrayList<>();
    }

    public boolean canPlayer(){
        return players.size() < this.maxPlayer;
    }

    public void addPlayer(Player _player)
    {
        if(this.canPlayer()){
            players.add(_player);
        }
    }

    public void deletePlayer(String _playerId){
        players.removeIf(player -> player.getId().equals(_playerId));
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

}
