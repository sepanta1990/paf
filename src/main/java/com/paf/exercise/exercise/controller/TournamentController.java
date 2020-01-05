package com.paf.exercise.exercise.controller;

import com.paf.exercise.exercise.dto.Player;
import com.paf.exercise.exercise.dto.Tournament;
import com.paf.exercise.exercise.exception.exception.RecordNotFoundException;
import com.paf.exercise.exercise.service.TournamentService;
import com.paf.exercise.exercise.util.mapper.PlayerMapper;
import com.paf.exercise.exercise.util.mapper.TournamentMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@RestController
@RequestMapping("/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper = Mappers.getMapper(TournamentMapper.class);
    private final PlayerMapper playerMapper = Mappers.getMapper(PlayerMapper.class);

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @ApiOperation(value = "Fetch all tournaments list")
    @GetMapping
    public ResponseEntity<List<Tournament>> getTournaments() {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentMapper.toTournamentDto(tournamentService.getTournaments()));
    }


    @ApiOperation(value = "Finds a particular tournament by id", response = Tournament.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the tournament object"),
            @ApiResponse(code = 404, message = "You have entered an invalid id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournament(@PathVariable("id") Integer id) {
        return tournamentService.getTournamentById(id).map(tournament -> ResponseEntity.ok(tournamentMapper.toTournamentDto(tournament)))
                .orElseThrow(() -> new RecordNotFoundException("Tournament not found with id: " + id));
    }

    @ApiOperation(value = "Update a particular tournament by id", response = Tournament.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the tournament object"),
            @ApiResponse(code = 404, message = "You have entered an invalid tournament id")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Tournament> updateTournament(@PathVariable("id") Integer id, @RequestBody Tournament tournament) {
        return tournamentService.updateTournament(id, tournament).map(tournamentResponse -> ResponseEntity.ok(tournamentMapper.toTournamentDto(tournamentResponse)))
                .orElseThrow(() -> new RecordNotFoundException("Tournament not found with id: " + id));
    }

    @ApiOperation(value = "Create a new tournament", response = Tournament.class)
    @PostMapping("/")
    public ResponseEntity<Tournament> addTournament(@RequestBody Tournament tournament) {
        return ResponseEntity.ok(tournamentMapper.toTournamentDto(tournamentService.addTournament(tournament)));
    }

    @ApiOperation(value = "Get players list of a particular tournament")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the players list of the tournament"),
            @ApiResponse(code = 404, message = "You have entered an invalid tournament id")
    })
    @GetMapping("/{id}/players")
    public ResponseEntity<List<Player>> getPlayers(@PathVariable("id") Integer id) {
        return tournamentService.getPlayersByTournamentId(id).map(players -> ResponseEntity.ok(playerMapper.toPlayerDto(new ArrayList<>(players))))
                .orElseThrow(() -> new RecordNotFoundException("Tournament not found with id: " + id));
    }

    @ApiOperation(value = "Delete a particular tournament")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the tournament"),
            @ApiResponse(code = 404, message = "You have entered an invalid tournament id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTournament(@PathVariable("id") Integer id) {
        if (!tournamentService.deleteTournamentById(id))
            throw new RecordNotFoundException("Tournament not found with id: " + id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "Adds a particular player to a particular tournament")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added player to the tournament"),
            @ApiResponse(code = 404, message = "You have entered an invalid tournament or player id")
    })
    @PostMapping("/{id}/players/{playerId}")
    public ResponseEntity<Tournament> addPlayerIntoTournament(@PathVariable("id") Integer id, @PathVariable("playerId") Integer playerId) {
        Optional<com.paf.exercise.exercise.entity.Tournament> tournament = tournamentService.getTournamentById(id);
        if (!tournament.isPresent())
            throw new RecordNotFoundException("Tournament not found with id: " + id);
        return tournamentService.addPlayerIntoTournament(tournament.get(), playerId).
                map(responseTournament -> ResponseEntity.ok(tournamentMapper.toTournamentDto(responseTournament))).
                orElseThrow(() -> new RecordNotFoundException("Player not found with id: " + playerId));
    }

    @ApiOperation(value = "Remove a particular player from a particular tournament")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully removed the player from the tournament"),
            @ApiResponse(code = 404, message = "You have entered an invalid tournament or player id")
    })
    @DeleteMapping("/{id}/players/{playerId}")
    public ResponseEntity<Void> removePlayerFromTournament(@PathVariable("id") Integer id, @PathVariable("playerId") Integer playerId) {
        tournamentService.deletePlayerFromTournament(id, playerId);
        return ResponseEntity.noContent().build();
    }
}
