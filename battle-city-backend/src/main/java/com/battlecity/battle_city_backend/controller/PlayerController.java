package com.battlecity.battle_city_backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PlayerController {

    private final List<String> players = new ArrayList<>();

    @MessageMapping("/add-player")
    @SendTo("/topic/players")
    public List<String> addPlayer(String playerName) {
        players.add(playerName);
        return players;
    }
}
