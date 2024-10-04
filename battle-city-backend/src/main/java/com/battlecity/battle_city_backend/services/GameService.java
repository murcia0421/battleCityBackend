package com.battlecity.battle_city_backend.services;

import com.battlecity.model.GameState;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private GameState gameState = new GameState();

    public void processPlayerAction(String playerId, String action) {
        // Aquí procesarás las acciones del jugador, por ejemplo mover el tanque o
        // disparar
        System.out.println("Procesando acción de " + playerId + ": " + action);

        // Actualizar el estado del juego
        updateGameState();
    }

    private void updateGameState() {
        // Lógica para actualizar el estado del juego (movimientos, colisiones, etc.)
    }

    public GameState getGameState() {
        return gameState;
    }
}
