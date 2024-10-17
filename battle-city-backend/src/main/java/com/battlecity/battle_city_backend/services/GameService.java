package com.battlecity.battle_city_backend.services;

import com.battlecity.model.PlayerAction;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    public void updatePlayerState(PlayerAction action) {
        System.out.println("Acción del jugador recibida: " + action);
    }
}
