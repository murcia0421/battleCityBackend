package com.battlecity.battle_city_backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
@Controller
public class GameController {

    @MessageMapping("/game-join")
    @SendTo("/topic/game-updates")
    public Object handleGameJoin(String message) {  // Cambiado a String para ver el mensaje raw
        try {
            System.out.println("Mensaje raw recibido: " + message);
            return message; // Devolver el mismo mensaje por ahora
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @MessageMapping("/game-move")
    @SendTo("/topic/game-updates")
    public Object handleGameMove(String moveMessage) {
        System.out.println("Movimiento recibido: " + moveMessage);
        return moveMessage;
    }
}