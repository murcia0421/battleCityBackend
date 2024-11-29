package com.battlecity.battle_city_backend.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WaitingRoomController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private Map<String, Room> rooms = new ConcurrentHashMap<>(Map.of(
            "room1", new Room("room1", "Room 1", 4),
            "room2", new Room("room2", "Room 2", 4),
            "room3", new Room("room3", "Room 3", 4),
            "room4", new Room("room4", "Room 4", 4)
    ));

    @MessageMapping("/join-room")
    @SendTo("/topic/rooms")
    public List<Room> joinRoom(RoomJoinRequest request) {
        Room room = rooms.get(request.getRoomId());
        if (room != null && room.canJoin()) {
            room.addPlayer(request.getName());
            broadcastRoomUpdates();
        }
        return new ArrayList<>(rooms.values());
    }

    private void broadcastRoomUpdates() {
        messagingTemplate.convertAndSend("/topic/rooms", new ArrayList<>(rooms.values()));
    }

    public static class Room {
        private String id;
        private String name;
        private int maxPlayers;
        private Set<String> players = new HashSet<>();

        public Room(String id, String name, int maxPlayers) {
            this.id = id;
            this.name = name;
            this.maxPlayers = maxPlayers;
        }

        public boolean canJoin() {
            return players.size() < maxPlayers;
        }

        public void addPlayer(String playerName) {
            players.add(playerName);
        }

        // Getters for id, name, maxPlayers, players
        public String getId() { return id; }
        public String getName() { return name; }
        public int getMaxPlayers() { return maxPlayers; }
        public Set<String> getPlayers() { return players; }
    }

    public static class RoomJoinRequest {
        private String name;
        private String roomId;

        public RoomJoinRequest(String name, String roomId) {
            this.name = name;
            this.roomId = roomId;
        }

        // Getters and setters for name and roomId
        public String getName() { return name; }
        public String getRoomId() { return roomId; }
        public void setName(String name) { this.name = name; }
        public void setRoomId(String roomId) { this.roomId = roomId; }
    }
}