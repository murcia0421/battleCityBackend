package com.battlecity.battle_city_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final Map<String, Integer> roomPlayers = new ConcurrentHashMap<>();

    public RoomController() {
        // Inicializamos todas las salas con 0 jugadores
        roomPlayers.clear();  // Aseguramos que el mapa esté vacío
        roomPlayers.put("room1", 0);
        roomPlayers.put("room2", 0);
        roomPlayers.put("room3", 0);
        roomPlayers.put("room4", 0);
    }

    @MessageMapping("/join")
    @SendTo("/topic/room-status")
    public synchronized Map<String, Object> joinRoom(String roomName) {
        logger.info("Attempting to join room: {}", roomName);

        Map<String, Object> response = new HashMap<>();

        if (roomPlayers.containsKey(roomName)) {
            int currentPlayers = roomPlayers.getOrDefault(roomName, 0);

            if (currentPlayers < 4) {
                int newPlayerCount = currentPlayers + 1;
                roomPlayers.put(roomName, newPlayerCount);
                logger.info("Player joined {}. Current players: {}", roomName, newPlayerCount);

                response.put("status", "success");
                response.put("message", "Player joined room successfully");
                response.put("roomName", roomName);
                response.put("currentPlayers", newPlayerCount);
            } else {
                logger.warn("Room {} is full!", roomName);
                response.put("status", "error");
                response.put("message", "Room is full!");
                response.put("roomName", roomName);
                response.put("currentPlayers", currentPlayers);
            }
        } else {
            logger.error("Room {} not found!", roomName);
            response.put("status", "error");
            response.put("message", "Room not found!");
            response.put("roomName", roomName);
            response.put("currentPlayers", 0);
        }

        return response;
    }

    @MessageMapping("/leave")
    @SendTo("/topic/room-status")
    public synchronized Map<String, Object> leaveRoom(String roomName) {
        logger.info("Attempting to leave room: {}", roomName);
        logger.info("Current room state before join: {}", roomPlayers);

        Map<String, Object> response = new HashMap<>();

        if (roomPlayers.containsKey(roomName)) {
            int currentPlayers = roomPlayers.get(roomName);
            int newPlayerCount = Math.max(0, currentPlayers - 1);
            roomPlayers.put(roomName, newPlayerCount);

            logger.info("Player left {}. Current players: {}", roomName, newPlayerCount);

            response.put("status", "success");
            response.put("message", "Player left room successfully");
            response.put("roomName", roomName);
            response.put("currentPlayers", newPlayerCount);
        } else {
            logger.error("Room {} not found!", roomName);
            response.put("status", "error");
            response.put("message", "Room not found!");
            response.put("roomName", roomName);
            response.put("currentPlayers", 0);
        }

        logger.info("Sending response: {}", response);
        return response;
    }

    public int getCurrentPlayers(String roomName) {
        return roomPlayers.getOrDefault(roomName, 0);
    }
}