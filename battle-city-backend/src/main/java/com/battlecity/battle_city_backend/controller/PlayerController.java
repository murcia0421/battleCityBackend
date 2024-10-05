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
        System.out.println("Acción del jugador recibida: " + action);
        gameService.updatePlayerState(action);
        GameState updatedGameState = gameService.getCurrentGameState();
        System.out.println("Estado del juego actualizado: " + updatedGameState.getPlayers());
        return updatedGameState;
    }

}
