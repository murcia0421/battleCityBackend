package com.example.battlecity_backend.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerActionController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerActionController.class);

    private final SimpMessagingTemplate messagingTemplate;

    public PlayerActionController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/player/action") // Endpoint para las acciones del jugador
    public void handlePlayerAction(@Payload PlayerAction playerAction) {
        // Log para verificar que el backend recibe el mensaje
        logger.info("Received player action: {}", playerAction);

        // Simulación de lógica adicional (procesar la acción, actualizar estado, etc.)
        logger.info("Processing action for room: {}", playerAction.getRoom());

        // Opcional: reenviar la acción a todos los clientes conectados a la sala
        messagingTemplate.convertAndSend("/topic/" + playerAction.getRoom(), playerAction);

        logger.info("Broadcasted action to room: {}", playerAction.getRoom());
    }

    public class PlayerAction {
        private String room;
        private boolean moveUp;
        private boolean moveDown;
        private boolean moveLeft;
        private boolean moveRight;
        private boolean shoot;

        // Getters y setters
        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public boolean isMoveUp() {
            return moveUp;
        }

        public void setMoveUp(boolean moveUp) {
            this.moveUp = moveUp;
        }

        public boolean isMoveDown() {
            return moveDown;
        }

        public void setMoveDown(boolean moveDown) {
            this.moveDown = moveDown;
        }

        public boolean isMoveLeft() {
            return moveLeft;
        }

        public void setMoveLeft(boolean moveLeft) {
            this.moveLeft = moveLeft;
        }

        public boolean isMoveRight() {
            return moveRight;
        }

        public void setMoveRight(boolean moveRight) {
            this.moveRight = moveRight;
        }

        public boolean isShoot() {
            return shoot;
        }

        public void setShoot(boolean shoot) {
            this.shoot = shoot;
        }

        @Override
        public String toString() {
            return "PlayerAction{" +
                    "room='" + room + '\'' +
                    ", moveUp=" + moveUp +
                    ", moveDown=" + moveDown +
                    ", moveLeft=" + moveLeft +
                    ", moveRight=" + moveRight +
                    ", shoot=" + shoot +
                    '}';
        }
    }

}