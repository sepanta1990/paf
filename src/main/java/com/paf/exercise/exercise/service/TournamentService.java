package com.paf.exercise.exercise.service;

import com.paf.exercise.exercise.entity.Player;
import com.paf.exercise.exercise.entity.Tournament;
import com.paf.exercise.exercise.repository.PlayerRepository;
import com.paf.exercise.exercise.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@Service
@Transactional
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;

    public TournamentService(TournamentRepository tournamentRepository, PlayerRepository playerRepository) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }

    public List<Tournament> getTournaments() {
        return tournamentRepository.findAll();
    }

}
