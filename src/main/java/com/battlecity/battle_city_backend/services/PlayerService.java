package com.battlecity.battle_city_backend.services;

import com.battlecity.battle_city_backend.repository.PlayerRepository;
import com.battlecity.model.Player;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void save(Player player) {
        playerRepository.save(player);
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public Optional<Player> findById(String id) {
        return playerRepository.findById(id);
    }

    public void deleteById(String id) {
        playerRepository.deleteById(id);
    }
}
