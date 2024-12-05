package com.battlecity.battle_city_backend.controller;

import com.battlecity.battle_city_backend.services.PlayerService;
import com.battlecity.battle_city_backend.services.PowerService;
import com.battlecity.model.Player;
import com.battlecity.model.PlayerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PlayerController {

    private final List<String> players = new ArrayList<>();
    private final PlayerService playerService;
    private final PowerService powerService;


    public PlayerController(PlayerService playerService, PowerService powerService) {
        this.playerService = playerService;
        this.powerService = powerService;
    }

    @MessageMapping("/add-player")
    @SendTo("/topic/players")
    public List<String> addPlayer(String playerName) {
        players.add(playerName);
        return players;
    }


    @PostMapping("/players")
    public void save(@RequestBody PlayerDTO playerDTO) {
        // Convierte el DTO a la entidad
        Player player = new Player();
        player.setId(playerDTO.getId());
        player.setName(playerDTO.getName());
        player.setPosition(playerDTO.getPosition());
        player.setDirection(playerDTO.getDirection());
        //player.setScore(playerDTO.getScore());

        playerService.save(player);
    }

    @GetMapping("/players")
    public List<PlayerDTO> findAll() {
        // Convierte la lista de entidades en una lista de DTOs
        return playerService.findAll().stream().map(player -> {
            PlayerDTO playerDTO = new PlayerDTO();
            playerDTO.setId(player.getId());
            playerDTO.setName(player.getName());
            playerDTO.setId(player.getId());
            playerDTO.setName(player.getName());
            playerDTO.setPosition(player.getPosition());
            playerDTO.setDirection(player.getDirection());
            //playerDTO.setScore(player.getScore());
            return playerDTO;
        }).toList();
    }

    @GetMapping("/player/{id}")
    public PlayerDTO findById(@PathVariable String id) {
        // Convierte la entidad a DTO
        Player player = playerService.findById(id).orElseThrow();
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(player.getId());
        playerDTO.setName(player.getName());
        playerDTO.setId(player.getId());
        playerDTO.setName(player.getName());
        playerDTO.setPosition(player.getPosition());
        playerDTO.setDirection(player.getDirection());
        //playerDTO.setScore(player.getScore());
        return playerDTO;
    }

    @DeleteMapping("/player/{id}")
    public void deleteById(@PathVariable String id){
        playerService.deleteById(id);
    }


    @PutMapping("/players")
    public void update(@RequestBody PlayerDTO playerDTO) {
        // Convierte el DTO a la entidad
        Player player = new Player();
        player.setId(playerDTO.getId());
        player.setName(playerDTO.getName());
        player.setPosition(playerDTO.getPosition());
        player.setDirection(playerDTO.getDirection());
        //player.setScore(playerDTO.getScore());

        playerService.save(player);
    }

    @MessageMapping("/collect-power")
    public void collectPower(int[] position) {
        powerService.collectPower(position[0], position[1]);
    }
}
