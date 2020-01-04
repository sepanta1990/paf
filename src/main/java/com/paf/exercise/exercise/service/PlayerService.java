package com.paf.exercise.exercise.service;

import com.paf.exercise.exercise.dto.Player;
import com.paf.exercise.exercise.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@Service
@Transactional
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public com.paf.exercise.exercise.entity.Player addPlayer(Player player) {
        com.paf.exercise.exercise.entity.Player playerEnt = new com.paf.exercise.exercise.entity.Player();
        playerEnt.setName(player.getName());
        return playerRepository.save(playerEnt);
    }

}
