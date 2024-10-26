package com.battlecity.battle_city_backend.controller;

import com.battlecity.battle_city_backend.model.Player; // Asegúrate de tener la clase Player
import com.battlecity.battle_city_backend.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Set;

@Controller
public class WaitingRoomController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private Set<String> players = new HashSet<>();

    @MessageMapping("/join")
    public void joinRoom(Player player) {
        players.add(player.getName());
        messagingTemplate.convertAndSend("/topic/players", players);
    }
}