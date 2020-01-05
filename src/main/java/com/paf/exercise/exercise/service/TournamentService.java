package com.paf.exercise.exercise.service;

import com.paf.exercise.exercise.entity.Tournament;
import com.paf.exercise.exercise.repository.PlayerRepository;
import com.paf.exercise.exercise.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

    public Optional<Tournament> getTournamentById(Integer id) {
        return Optional.ofNullable(tournamentRepository.findOne(id));
    }

    public Optional<Tournament> updateTournament(Integer id, com.paf.exercise.exercise.dto.Tournament tournament) {
        return Optional.ofNullable(tournamentRepository.findOne(id)).map(tournamentEnt -> {
            tournamentEnt.setRewardAmount(tournament.getRewardAmount());
            return tournamentRepository.save(tournamentEnt);
        });
    }

    public com.paf.exercise.exercise.entity.Tournament addTournament(com.paf.exercise.exercise.dto.Tournament tournament) {
        com.paf.exercise.exercise.entity.Tournament newTournament = new com.paf.exercise.exercise.entity.Tournament();
        newTournament.setRewardAmount(tournament.getRewardAmount());
        return tournamentRepository.save(newTournament);
    }


}
