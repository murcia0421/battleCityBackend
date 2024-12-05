package com.battlecity.battle_city_backend.services;

import com.battlecity.model.Power;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PowerService {

    private final int[][] mapData = {
            {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 2, 2, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 1, 0, 3, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 3, 0, 1, 0, 0, 1, 0, 1},
            {1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 0, 2, 2, 1, 0, 2, 2, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 3, 0, 0, 0, 0, 1, 0, 3, 0, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1},
            {0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
            {2, 2, 2, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 2, 2, 0, 2, 2, 2},
            {2, 0, 4, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 2, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 2, 2, 0, 2, 2, 2},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 2, 2, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 1, 0, 3, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 3, 0, 1, 0, 0, 1, 0, 1},
            {1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 0, 2, 2, 1, 0, 2, 2, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 3, 0, 0, 0, 0, 1, 0, 3, 0, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1},
            {0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
            {2, 2, 2, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 2, 2, 0, 2, 2, 2},
            {2, 0, 4, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 2, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 2, 2, 0, 2, 2, 2},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 2, 2, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1}
    };

    private final List<Power> powers = new ArrayList<>();
    private final SimpMessagingTemplate messagingTemplate;

    private final Random random = new Random();

    public PowerService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 10000) // Cada 10 segundos
    public void generatePower() {
        // Buscar una posición aleatoria en el mapa que sea 0
        int row, col;
        do {
            row = random.nextInt(mapData.length);
            col = random.nextInt(mapData[0].length);
        } while (mapData[row][col] != 0); // Continuar hasta encontrar un espacio vacío

        // Crear un nuevo poder
        Power newPower = new Power(row, col, "speed"); // Tipo de poder puede variar
        powers.add(newPower);

        // Notificar a todos los clientes
        messagingTemplate.convertAndSend("/topic/powers", newPower);
    }

    public void collectPower(int row, int col) {
        // Buscar el poder en la posición dada
        powers.removeIf(power -> power.getRow() == row && power.getCol() == col);

        // Notificar a todos los clientes que el poder fue recogido
        messagingTemplate.convertAndSend("/topic/power-collected", new int[] { row, col });
    }

    public int[][] getMapData() {
        return mapData;
    }

    public List<Power> getActivePowers() {
        return new ArrayList<>(powers);
    }
}
