package com.battlecity.battle_city_backend.controller;

import com.battlecity.battle_city_backend.services.PlayerService;
import com.battlecity.battle_city_backend.services.PowerService;
import com.battlecity.model.Player;
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

    @MessageMapping("/add-player")
    @SendTo("/topic/players")
    public List<String> addPlayer(String playerName) {
        players.add(playerName);
        return players;
    }

    @PostMapping("/players")
    public void save(@RequestBody Player player){
        playerService.save(player);
    }

    @GetMapping("/players")
    public List<Player> findAll(){
        return playerService.findAll();
    }

    @GetMapping("/player/{id}")
    public Player findById(@PathVariable String id){
        return playerService.findById(id).get();
    }

    @DeleteMapping("/player/{id}")
    public void deleteById(@PathVariable String id){
        playerService.deleteById(id);
    }

    @PutMapping("/players")
    public void update(@RequestBody Player player){
        playerService.save(player);
    }

    @MessageMapping("/collect-power")
    public void collectPower(int[] position) {
        powerService.collectPower(position[0], position[1]);
    }
}
