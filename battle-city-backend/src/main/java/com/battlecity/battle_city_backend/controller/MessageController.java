package com.battlecity.battle_city_backend.controller;

import com.battlecity.model.Player;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class MessageController {

    private final List<Player> players = new CopyOnWriteArrayList<>();
    private static final int MAX_PLAYERS = 4;

    @MessageMapping("/players")
    @SendTo("/topic/players")
    public Player handlePlayerMessage(Player player) {
        System.out.println("Mensaje recibido en el servidor"); // Log para debug

        // Crear nuevo jugador
        String playerId = "Jugador " + (players.size() + 1);
        Player newPlayer = new Player(playerId, player.getName()    );

        // Añadir a la lista si no existe
        if (!players.contains(newPlayer)) {
            players.add(newPlayer);
            System.out.println("Nuevo jugador añadido: " + playerId); // Log para debug
            System.out.println("Total jugadores: " + players.size()); // Log para debug
        }

        return newPlayer;
    }


    @MessageMapping("/leave")
    @SendTo("/topic/players")
    public void handlePlayerLeave(String playerId) {
        players.removeIf(p -> p.getId().equals(playerId));
        System.out.println("Jugador eliminado: " + playerId);
        System.out.println("Total de jugadores: " + players.size());
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
        System.out.println("Solicitada lista de jugadores actual. Total: " + players.size());
        return players;
    }
}