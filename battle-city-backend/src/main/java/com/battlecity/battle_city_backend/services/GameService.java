package com.battlecity.battle_city_backend.services;
@Service
public class GameService {

    public void updatePlayerState(PlayerAction action) {
        System.out.println("Acción del jugador recibida: " + action);
    }
}
