package com.battlecity.battle_city_backend.services;

import com.battlecity.model.GameState;
import com.battlecity.model.Player;
import com.battlecity.model.PlayerAction;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private Map<String, Player> players = new HashMap<>();
    private GameState gameState = new GameState();

    public GameService() {
        players.put("playerId1", new Player("playerId1", 5, 5, "up"));
    }

    public Player findPlayerById(String id) {
        return players.get(id);
    }

    public void updatePlayerState(PlayerAction action) {
        Player player = findPlayerById(action.getPlayerId());

        if (player == null) {
            System.out.println("Jugador no encontrado: " + action.getPlayerId());
            return;
        }

        System.out.println("Antes de mover - Posición del jugador: " + player.getX() + ", " + player.getY());

        if (action.getType().equals("MOVE")) {
            switch (action.getDirection()) {
                case "up":
                    player.setY(player.getY() - 1); // Mover hacia arriba
                    break;
                case "down":
                    player.setY(player.getY() + 1); // Mover hacia abajo
                    break;
                case "left":
                    player.setX(player.getX() - 1); // Mover hacia la izquierda
                    break;
                case "right":
                    player.setX(player.getX() + 1); // Mover hacia la derecha
                    break;
            }
        }

        System.out.println("Después de mover - Posición del jugador: " + player.getX() + ", " + player.getY());
    }

    public GameState getCurrentGameState() {
        gameState.setPlayers(players);
        System.out.println("Estado actual del juego: " + gameState.getPlayers());
        return gameState;
    }
}
