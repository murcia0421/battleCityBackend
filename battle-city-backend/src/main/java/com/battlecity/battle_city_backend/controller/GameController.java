package com.battlecity.battle_city_backend.controller;

import com.battlecity.model.GameRoom;
import com.battlecity.model.PlayerMove;
import com.battlecity.model.PlayerShot;
import com.battlecity.model.JoinRequest;
import com.battlecity.battle_city_backend.services.GameRoomService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private final GameRoomService gameRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    public GameController(GameRoomService gameRoomService, SimpMessagingTemplate messagingTemplate) {
        this.gameRoomService = gameRoomService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/join")
    public void joinRoom(@Payload JoinRequest joinRequest) {
        GameRoom room = gameRoomService.addPlayerToRoom(joinRequest.getPlayerId(), joinRequest.getPosition(),
                joinRequest.getDirection());
        messagingTemplate.convertAndSend("/topic/" + room.getRoomId() + "/players",
                joinRequest.getPlayerId() + " joined the game.");
    }

    @MessageMapping("/move")
    public void handleMove(@Payload PlayerMove move) {
        String roomId = move.getRoomId();
        messagingTemplate.convertAndSend("/topic/" + roomId + "/moves", move);
    }

    @MessageMapping("/shoot")
    public void handleShot(@Payload PlayerShot shot) {
        String roomId = shot.getRoomId();
        messagingTemplate.convertAndSend("/topic/" + roomId + "/shots", shot);
    }
}
