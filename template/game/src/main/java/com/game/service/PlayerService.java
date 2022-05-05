package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Integer countAll (Specification<Player> specification) {
        return (int) playerRepository.count(specification);
    }

    public Page<Player> getPlayersWithSpecs(Specification<Player> specification, Pageable pageable) {
        return playerRepository.findAll(specification, pageable);
    }

    public Boolean isNewObjectValid(Player player) {
        if(player.getName().length() < 1 || player.getName().length() > 12
                || player.getTitle().length() < 1 || player.getTitle().length() > 30
                || player.getName().isEmpty()
                || player.getExperience() < 0 || player.getExperience() > 10_000_000
                || player.getBirthday().getTime() < 0
                || player.getBirthday().getYear() < 100
                || player.getBirthday().getYear() > 1100) {
            return false;
        }
        else{
            return true;
        }
    }

    public void createPlayer(Player player) {
        player.setLevelAndUntilNext(player.getExperience());
        playerRepository.save(player);
    }
}
