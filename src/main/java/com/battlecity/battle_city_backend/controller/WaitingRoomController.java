package com.battlecity.battle_city_backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.battlecity.battle_city_backend.services.GameRoomService;
import com.battlecity.model.GameRoom;
import com.battlecity.model.Player;

@Controller
@RequestMapping("/api/waiting-room")
public class WaitingRoomController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final GameRoomService gameRoomService;

    public WaitingRoomController(GameRoomService gameRoomService, SimpMessagingTemplate messagingTemplate) {
        this.gameRoomService = gameRoomService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/joinRoom")
    public void joinRoom(String roomId, Player player) {
        boolean success = gameRoomService.addPlayerToRoom(roomId, player);

        if (success) {
            GameRoom gameRoom = gameRoomService.getRoom(roomId);
            messagingTemplate.convertAndSend("/topic/room/" + roomId, gameRoom); // Dynamic topic
        } else {
            messagingTemplate.convertAndSend("/topic/room/" + roomId, "Unable to connect to the room"); // More informative message
        }
    }

    @GetMapping("/rooms")
    public List<Player> getPlayersInRoom() {
        // Assuming room "room1" is a placeholder, this could be made dynamic in real-world scenarios
        return gameRoomService.getPlayersInRoom("room1");
    }
}
