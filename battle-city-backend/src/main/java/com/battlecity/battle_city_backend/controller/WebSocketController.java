package com.battlecity.battle_city_backend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@RestController
public class WebSocketController extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parsear el mensaje del jugador
        String payload = message.getPayload();
        // Llamar al servicio para manejar la acción del jugador (moverse, disparar,
        // etc.)
        // Responder el estado actualizado del juego
        session.sendMessage(new TextMessage("Acción procesada: " + payload));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Conexión establecida: " + session.getId());
    }
}
