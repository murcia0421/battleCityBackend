// GameService.java
package com.battlecity.battle_city_backend.services;

import com.battlecity.model.GameState;
import com.battlecity.model.Player;
import com.battlecity.model.PlayerAction;
import com.battlecity.model.Bullet;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Service
public class GameService {
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    private final List<Bullet> bullets = new ArrayList<>();

    public GameState handlePlayerAction(PlayerAction action) {
        switch (action.getType()) {
            case "PLAYER_UPDATE":
                updatePlayerState(action);
                return createGameState("PLAYER_UPDATE", action.getPlayerId());
            case "BULLET_UPDATE":
                handleBulletUpdate(action);
                return createGameState("BULLET_UPDATE", action.getPlayerId());
            default:
                return getCurrentGameState();
        }
    }

    public GameState addPlayer(Player player) {
        players.put(player.getId(), player);
        return createGameState("PLAYER_JOIN", player.getId());
    }

    public GameState removePlayer(String playerId) {
        players.remove(playerId);
        return createGameState("PLAYER_DISCONNECT", playerId);
    }

    private void updatePlayerState(PlayerAction action) {
        Player player = players.get(action.getPlayerId());
        if (player != null) {
            player.setPosition(action.getPosition());
            player.setDirection(action.getDirection());
            players.put(action.getPlayerId(), player);
        }
    }

    private void handleBulletUpdate(PlayerAction action) {
        if (action.getBullet() != null) {
            bullets.add(action.getBullet());
        }
    }

    private GameState createGameState(String type, String playerId) {
        GameState gameState = new GameState();
        gameState.setType(type);
        gameState.setPlayerId(playerId);
        gameState.setPlayers(new HashMap<>(players));
        gameState.setBullets(new ArrayList<>(bullets));
        return gameState;
    }

    public GameState getCurrentGameState() {
        return createGameState("STATE_UPDATE", null);
    }
}