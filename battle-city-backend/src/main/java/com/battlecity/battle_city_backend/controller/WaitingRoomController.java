package com.battlecity.battle_city_backend.controller;

import java.util.HashSet;
import java.util.Set;

import com.battlecity.battle_city_backend.services.GameRoomService;
import com.battlecity.model.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.battlecity.model.Player;

@Controller
public class WaitingRoomController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final GameRoomService gameRoomService;

    public WaitingRoomController(GameRoomService gameRoomService, SimpMessagingTemplate _messagingTemplate) {
        this.gameRoomService = gameRoomService;
        this.messagingTemplate = _messagingTemplate;
    }

    @MessageMapping("/joinRoom")
    public void joinRoom(String _roomId, Player _player)
    {
        GameRoom gameRoom;
        boolean success = gameRoomService.addPlayerToRoom(_roomId, _player);
        if(success){
            gameRoom = gameRoomService.getRoom(_roomId);
            messagingTemplate.convertAndSend("/topic/roomId/",gameRoom);
        }
        else {
            messagingTemplate.convertAndSend("/topic/roomId/", "No se pudo conectar");
        }

    }



    //private Set<String> players = new HashSet<>();
    /*@MessageMapping("/join")
    public void joinRoom(Player player) {
        players.add(player.getName());
        messagingTemplate.convertAndSend("/topic/players", players);
    }*/
}