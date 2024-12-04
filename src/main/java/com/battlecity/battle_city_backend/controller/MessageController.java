package com.battlecity.battle_city_backend.controller;

import com.battlecity.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final List<Player> players = new CopyOnWriteArrayList<>();
    private static final int MAX_PLAYERS = 4;

    @MessageMapping("/players")
    @SendTo("/topic/players")
    public Player handlePlayerMessage(Player player) {
        logger.info("Mensaje recibido en el servidor");

        // Only log non-sensitive or system-controlled information
        logger.info("Recibido color del tanque. Nombre del jugador no registrado para evitar filtración.");

        String playerId = "Jugador " + (players.size() + 1);
        Player newPlayer = new Player(playerId, player.getName(), player.getTankColor());

        if (!players.contains(newPlayer)) {
            players.add(newPlayer);
            logger.info("Nuevo jugador añadido con ID interno: {}", playerId);
            logger.info("Total jugadores en la lista: {}", players.size());
        }

        return newPlayer;
    }

    @MessageMapping("/leave")
    @SendTo("/topic/players")
    public void handlePlayerLeave(String playerId) {
        players.removeIf(p -> p.getId().equals(playerId));

        // Log internal identifiers only, avoid logging actual player ID from user input
        logger.info("Jugador con ID interno eliminado.");
        logger.info("Total de jugadores restantes: {}", players.size());
    }

    public List<Player> getPlayers() {
        return List.copyOf(players);
    }

    public boolean isFull() {
        return players.size() >= MAX_PLAYERS;
    }

    @MessageMapping("/request-players")
    @SendTo("/topic/players")
    public List<Player> getPlayersStatus() {
        logger.info("Solicitud de estado de jugadores recibida. Total de jugadores en la lista: {}", players.size());
        return players;
    }
}
