package com.battlecity.battle_city_backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.battlecity.model.PlayerMove;
import com.battlecity.model.PlayerShot;

@Controller
public class GameController {

    @MessageMapping("/move")
    @SendTo("/topic/moves")
    public PlayerMove handleMove(PlayerMove move) {
        // Procesa el movimiento y lo retransmite a todos los clientes
        return move;
    }

    @MessageMapping("/shoot")
    @SendTo("/topic/shots")
    public PlayerShot handleShot(PlayerShot shot) {
        // Procesa el disparo y lo retransmite a todos los clientes
        return shot;
    }
}
