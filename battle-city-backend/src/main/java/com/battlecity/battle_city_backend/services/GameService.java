package com.battlecity.battle_city_backend.services;

import com.battlecity.model.GameState;
import com.battlecity.model.PlayerAction;

import org.springframework.stereotype.Service;

@Service
public class GameService {

    private GameState gameState = new GameState();

    public void updatePlayerState(PlayerAction action) {
        // Actualiza la posición del jugador dependiendo de la acción
        switch (action.getType()) {
            case "MOVE":
                // Obtén el jugador por su ID y actualiza la posición según la dirección
                break;
            case "SHOOT":
                // Implementa la lógica para disparar
                break;
            default:
                break;
        }
    }

    public GameState getCurrentGameState() {
        return gameState;
    }
}
