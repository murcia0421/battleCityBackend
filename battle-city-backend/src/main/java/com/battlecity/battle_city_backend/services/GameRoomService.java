package com.battlecity.battle_city_backend.services;

import com.battlecity.model.GameRoom;
import com.battlecity.model.Player;
import com.battlecity.model.Position;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameRoomService {
    private Map<String, GameRoom> rooms;


    public GameRoomService() {

        rooms = new HashMap<>();
    }

    public void initialiceRoom(){
        rooms.put("room1", new GameRoom("room1", 2));
        rooms.put("room2", new GameRoom("room2", 2));
        rooms.put("room3", new GameRoom("room3", 2));
        rooms.put("room4", new GameRoom("room4", 2));
    }

    public boolean addPlayerToRoom(String _roomId, Player _player){
        GameRoom gameRoom = rooms.get(_roomId);

        if(gameRoom != null && gameRoom.canPlayer()){
            gameRoom.addPlayer(_player);
            return true;
        }
        return false;
    }

    public GameRoom getRoom(String _roomId){
        return rooms.get(_roomId);
    }

    public void removePlayerToRoom(String _roomId, Player _player){
        GameRoom gameRoom = rooms.get(_roomId);

        if(gameRoom != null ){
            gameRoom.deletePlayer(_player.getId());
        }
    }

    public List<Player> getPlayersInRoom(String _roomId){
        GameRoom gameRoom = rooms.get(_roomId);
        return gameRoom != null ? gameRoom.getPlayers() : null ;
    }



}
