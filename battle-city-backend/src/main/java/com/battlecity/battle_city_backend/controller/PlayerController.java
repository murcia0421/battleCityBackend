// PlayerController.java
package com.battlecity.battle_city_backend.controller;

import com.battlecity.battle_city_backend.services.GameService;
import com.battlecity.model.GameState;
import com.battlecity.model.PlayerAction;
import com.battlecity.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerController {

    @Autowired
    private GameService gameService;

    @MessageMapping("/player-action")
    @SendTo("/topic/game-updates")
    public GameState handlePlayerAction(PlayerAction action) {
        System.out.println("Player action received: " + action);
        return gameService.handlePlayerAction(action);
    }

    @MessageMapping("/player-join")
    @SendTo("/topic/game-updates")
    public GameState handlePlayerJoin(Player player) {
        System.out.println("New player joined: " + player.getId());
        return gameService.addPlayer(player);
    }

    @MessageMapping("/player-disconnect")
    @SendTo("/topic/game-updates")
    public GameState handlePlayerDisconnect(Player player) {
        System.out.println("Player disconnected: " + player.getId());
        return gameService.removePlayer(player.getId());
    }
}