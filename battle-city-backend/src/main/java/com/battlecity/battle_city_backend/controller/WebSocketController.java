package com.battlecity.battle_city_backend.controller;

import com.battlecity.battle_city_backend.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.battlecity.model.PlayerAction;

@RestController
public class WebSocketController extends TextWebSocketHandler {

    @Autowired
    private GameService gameService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parsear el mensaje del jugador
        String payload = message.getPayload();
        PlayerAction action = objectMapper.readValue(payload, PlayerAction.class);

        // Llamar al servicio para manejar la acción del jugador
        gameService.updatePlayerState(action);

        // Obtener el estado actualizado del juego
        String updatedGameState = objectMapper.writeValueAsString(gameService.getCurrentGameState());

        // Responder el estado actualizado del juego
        session.sendMessage(new TextMessage(updatedGameState));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Conexión establecida: " + session.getId());
    }
}
