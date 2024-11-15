package com.battlecity.battle_city_backend.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.AckRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Configuration
public class SocketIOConfig {

    private final Map<String, AtomicInteger> roomCapacities = Map.of(
            "room1", new AtomicInteger(2),
            "room2", new AtomicInteger(2),
            "room3", new AtomicInteger(4),
            "room4", new AtomicInteger(4));
    private final Map<String, AtomicInteger> roomPlayers = new ConcurrentHashMap<>();

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("localhost");
        config.setPort(8080);

        SocketIOServer server = new SocketIOServer(config);

        // Listener para conexiones
        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
            }
        });

        server.addEventListener("joinRoom", Map.class, new DataListener<Map>() {
            @Override
            public void onData(SocketIOClient client, Map data, AckRequest ackRequest) {
                String name = (String) data.get("name");
                String room = (String) data.get("room");

                if (room != null && roomCapacities.containsKey(room)) {
                    int maxCapacity = roomCapacities.get(room).get();
                    roomPlayers.putIfAbsent(room, new AtomicInteger(0));
                    int currentCount = roomPlayers.get(room).get();

                    if (currentCount < maxCapacity) {
                        client.joinRoom(room);
                        roomPlayers.get(room).incrementAndGet();
                        System.out.println("Cliente conectado a " + room + ": " + client.getSessionId());

                        // Actualizar la lista de jugadores en la sala
                        List<String> players = server.getRoomOperations(room).getClients().stream()
                                .map(c -> c.getHandshakeData().getSingleUrlParam("name"))
                                .collect(Collectors.toList());
                        server.getRoomOperations(room).sendEvent("players", players);

                        server.getRoomOperations(room).sendEvent("mensaje",
                                "¡El jugador " + name + " se ha unido a " + room + "!");
                    } else {
                        client.sendEvent("error", "La sala " + room + " está llena.");
                    }
                } else {
                    client.sendEvent("error", "Sala especificada no existe.");
                }
            }
        });

        // Listener para desconexión
        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                String room = client.getAllRooms().stream().findFirst().orElse(null);
                if (room != null && roomPlayers.containsKey(room)) {
                    roomPlayers.get(room).decrementAndGet();
                    System.out.println("Cliente desconectado de " + room + ": " + client.getSessionId());

                    // Actualizar la lista de jugadores en la sala
                    List<String> players = server.getRoomOperations(room).getClients().stream()
                            .map(c -> c.getHandshakeData().getSingleUrlParam("name"))
                            .collect(Collectors.toList());
                    server.getRoomOperations(room).sendEvent("players", players);
                }
            }
        });

        return server;
    }
}