package com.paf.exercise.exercise.service;

import com.paf.exercise.exercise.entity.Player;
import com.paf.exercise.exercise.entity.Tournament;
import com.paf.exercise.exercise.repository.TournamentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TournamentServiceTest {

    @MockBean
    private TournamentRepository tournamentRepository;

    @Autowired
    private TournamentService tournamentService;

    @Test
    public void getTournamentsTest() {
        when(tournamentRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(tournamentService.getTournaments().isEmpty());

        Tournament tournament = new Tournament(1, 5, null);
        when(tournamentRepository.findAll()).thenReturn(Collections.singletonList(tournament));
        assertArrayEquals(Collections.singletonList(tournament).toArray(), tournamentService.getTournaments().toArray());

    }

    @Test
    public void getTournamentByIdTest() {
        Tournament tournament = new Tournament(1, 5, null);

        when(tournamentRepository.findOne(1)).thenReturn(tournament);
        assertEquals(Optional.of(tournament), tournamentService.getTournamentById(1));

        when(tournamentRepository.findOne(2)).thenReturn(null);
        assertEquals(Optional.empty(), tournamentService.getTournamentById(2));
    }

    @Test
    public void updateTournamentTest() {
        Tournament tournamentToBeUpdated = new Tournament(1, 5, null);
        Tournament tournamentUpdated = new Tournament(1, 10, null);


        when(tournamentRepository.save(tournamentToBeUpdated)).thenReturn(tournamentUpdated);
        when(tournamentRepository.findOne(1)).thenReturn(tournamentToBeUpdated);

        assertEquals(Optional.of(tournamentUpdated), tournamentService.updateTournament(1, new com.paf.exercise.exercise.dto.Tournament(1, 10, null)));
    }

    @Test
    public void addTournamentTest() {
        Tournament tournamentToBeSaved = new Tournament(null, 10, null);
        Tournament tournamentSaved = new Tournament(1, 10, null);


        when(tournamentRepository.save(tournamentToBeSaved)).thenReturn(tournamentSaved);

        assertEquals(tournamentSaved, tournamentService.addTournament(new com.paf.exercise.exercise.dto.Tournament(null, 10, null)));
    }

    @Test
    public void getPlayersByTournamentIdTest() {

        when(tournamentRepository.findOne(1)).thenReturn(null);
        assertEquals(Optional.empty(), tournamentService.getPlayersByTournamentId(1));

        when(tournamentRepository.findOne(1)).thenReturn(new Tournament());
        assertFalse(tournamentService.getPlayersByTournamentId(1).isPresent());

        Player player1 = new Player();
        player1.setName("Mohammad");
        player1.setId(1);

        List<Player> players = Collections.singletonList(player1);

        Tournament tournament1 = new Tournament();
        tournament1.setRewardAmount(10);
        tournament1.setId(1);
        tournament1.setPlayers(new HashSet<>(players));

        when(tournamentRepository.findOne(1)).thenReturn(tournament1);

        assertArrayEquals(players.toArray(), tournamentService.getPlayersByTournamentId(1).get().toArray());

    }
}
