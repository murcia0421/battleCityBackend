package com.battlecity.battle_city_backend.repository;

import com.battlecity.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository  extends MongoRepository<Player, String> {
}