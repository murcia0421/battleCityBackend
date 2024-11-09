package com.battlecity.battle_city_backend.controller;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class WaitingRoomController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private Set<String> players = new HashSet<>();

    /*@MessageMapping("/join")
    public void joinRoom(Player player) {
        players.add(player.getName());
        messagingTemplate.convertAndSend("/topic/players", players);
    }*/
}