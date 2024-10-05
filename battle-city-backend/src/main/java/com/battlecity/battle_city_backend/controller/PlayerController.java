package com.battlecity.battle_city_backend.controller;

import com.battlecity.model.GameState;
import com.battlecity.model.PlayerAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.battlecity.battle_city_backend.services.GameService;

@Controller
public class PlayerController {

    @Autowired
    private GameService gameService;

    @MessageMapping("/player-action")
    @SendTo("/topic/game-updates")
    public GameState handlePlayerAction(PlayerAction action) {
        gameService.updatePlayerState(action);
        return gameService.getCurrentGameState();
    }
}
