package com.paf.exercise.exercise.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paf.exercise.exercise.entity.Player;
import com.paf.exercise.exercise.entity.Tournament;
import com.paf.exercise.exercise.service.TournamentService;
import com.paf.exercise.exercise.util.mapper.TournamentMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TournamentController.class)
public class TournamentControllerTest {
    private final TournamentMapper tournamentMapper = Mappers.getMapper(TournamentMapper.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void setup() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TournamentService tournamentService;

    @Test
    public void getTournamentsTest() throws Exception {

        when(tournamentService.getTournaments()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/tournaments")).andExpect(status().isOk())
                .andExpect(content().json(String.valueOf(new ArrayList<>())));

        Player player1 = new Player();
        player1.setName("Mohammad");
        player1.setId(1);

        Player player2 = new Player();
        player2.setName("Robert");
        player2.setId(2);

        Tournament tournament1 = new Tournament();
        tournament1.setRewardAmount(10);
        tournament1.setId(1);
        tournament1.setPlayers(new HashSet<>(Arrays.asList(player1, player2)));

        Tournament tournament2 = new Tournament();
        tournament2.setRewardAmount(11);
        tournament2.setId(2);

        Tournament tournament3 = new Tournament();
        tournament3.setRewardAmount(11);
        tournament3.setId(3);

        when(tournamentService.getTournaments()).thenReturn(Arrays.asList(tournament1, tournament2, tournament3));
        MvcResult requestResult = this.mockMvc.perform(get("/tournaments")).andExpect(status().isOk()).andReturn();
        List<com.paf.exercise.exercise.dto.Tournament> tournamentList = mapper.readValue(requestResult.getResponse().getContentAsString(), new TypeReference<List<com.paf.exercise.exercise.dto.Tournament>>() {
        });
        assertArrayEquals(tournamentList.toArray(), tournamentMapper.toTournamentDto(Arrays.asList(tournament1, tournament2, tournament3)).toArray());

        when(tournamentService.getTournaments()).thenReturn(Collections.singletonList(tournament3));
        this.mockMvc.perform(get("/tournaments")).andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(tournamentMapper.toTournamentDto(Collections.singletonList(tournament3)))));

        when(tournamentService.getTournaments()).thenReturn(Arrays.asList(tournament2, tournament3));
        requestResult = this.mockMvc.perform(get("/tournaments")).andExpect(status().isOk()).andReturn();
        tournamentList = mapper.readValue(requestResult.getResponse().getContentAsString(), new TypeReference<List<com.paf.exercise.exercise.dto.Tournament>>() {
        });
        assertArrayEquals(tournamentList.toArray(), tournamentMapper.toTournamentDto(Arrays.asList(tournament2, tournament3)).toArray());

    }

    @Test
    public void getTournamentShouldReturnOK() throws Exception {
        Player player1 = new Player();
        player1.setName("Mohammad");
        player1.setId(1);

        Player player2 = new Player();
        player2.setName("Robert");
        player2.setId(2);

        Tournament tournament1 = new Tournament();
        tournament1.setRewardAmount(10);
        tournament1.setId(1);
        tournament1.setPlayers(new HashSet<>(Arrays.asList(player1, player2)));

        when(tournamentService.getTournamentById(1)).thenReturn(Optional.of(tournament1));
        MvcResult requestResult = this.mockMvc.perform(get("/tournaments/1")).andExpect(status().isOk())
                .andExpect(status().isOk()).andReturn();
        assertEquals(parseResponse(requestResult, com.paf.exercise.exercise.dto.Tournament.class),
                tournamentMapper.toTournamentDto(tournament1));
        verify(tournamentService, times(1)).getTournamentById(1);

        tournament1.setPlayers(null);
        when(tournamentService.getTournamentById(2)).thenReturn(Optional.of(tournament1));
        requestResult = this.mockMvc.perform(get("/tournaments/2")).andExpect(status().isOk())
                .andExpect(status().isOk()).andReturn();
        assertEquals(parseResponse(requestResult, com.paf.exercise.exercise.dto.Tournament.class),
                tournamentMapper.toTournamentDto(tournament1));
        verify(tournamentService, times(1)).getTournamentById(2);
    }

    @Test
    public void getTournamentShouldReturnNotfound() throws Exception {
        when(tournamentService.getTournamentById(1)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/tournaments/1")).andExpect(status().isNotFound());
        verify(tournamentService, times(1)).getTournamentById(1);
    }

    @Test
    public void updateTournamentShouldReturnOK() throws Exception {
        Player player1 = new Player();
        player1.setName("Mohammad");
        player1.setId(1);

        Player player2 = new Player();
        player2.setName("Robert");
        player2.setId(2);

        Tournament tournament1 = new Tournament();
        tournament1.setRewardAmount(10);
        tournament1.setId(1);
        tournament1.setPlayers(new HashSet<>(Arrays.asList(player1, player2)));


        com.paf.exercise.exercise.dto.Tournament requestDto = new com.paf.exercise.exercise.dto.Tournament(null, 10, null);
        when(tournamentService.updateTournament(1, requestDto)).thenReturn(Optional.of(tournament1));
        MvcResult requestResult = this.mockMvc.perform(put("/tournaments/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk()).andReturn();

        com.paf.exercise.exercise.dto.Tournament responseDto = parseResponse(requestResult, com.paf.exercise.exercise.dto.Tournament.class);
        assertEquals(responseDto.getRewardAmount(), requestDto.getRewardAmount());
        assertEquals(responseDto, tournamentMapper.toTournamentDto(tournament1));
    }

    @Test
    public void updateTournamentShouldReturnNotfound() throws Exception {
        com.paf.exercise.exercise.dto.Tournament requestDto = new com.paf.exercise.exercise.dto.Tournament(null, 10, null);
        when(tournamentService.updateTournament(1, requestDto)).thenReturn(Optional.empty());
        this.mockMvc.perform(put("/tournaments/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isNotFound());

    }

    @Test
    public void addTournamentTest() throws Exception {
        com.paf.exercise.exercise.dto.Tournament requestDto = new com.paf.exercise.exercise.dto.Tournament(null, 10, null);
        when(tournamentService.addTournament(requestDto)).thenReturn(new Tournament(2, 10, null));
        MvcResult requestResult = this.mockMvc.perform(post("/tournaments/").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk()).andReturn();

        com.paf.exercise.exercise.dto.Tournament responseDto = parseResponse(requestResult, com.paf.exercise.exercise.dto.Tournament.class);
        assertEquals(responseDto.getRewardAmount(), requestDto.getRewardAmount());
        assertEquals(responseDto.getId(), new Integer(2));
        assertNull(responseDto.getPlayers());

    }

    @Test
    public void getPlayersShouldReturnOK() throws Exception {

        when(tournamentService.getPlayersByTournamentId(1)).thenReturn(Optional.of(new HashSet<>()));
        MvcResult requestResult = this.mockMvc.perform(get("/tournaments/1/players"))
                .andExpect(status().isOk()).andReturn();


        List<com.paf.exercise.exercise.dto.Player> playerList = mapper.readValue(requestResult.getResponse().getContentAsString(), new TypeReference<List<com.paf.exercise.exercise.dto.Player>>() {
        });
        assertTrue(playerList.isEmpty());


        when(tournamentService.getPlayersByTournamentId(1)).thenReturn(Optional.of(new HashSet<>(
                Arrays.asList(new Player(1, "Mohammad", null), new Player(2, "Robert", null))
        )));
        requestResult = this.mockMvc.perform(get("/tournaments/1/players"))
                .andExpect(status().isOk()).andReturn();

        playerList = mapper.readValue(requestResult.getResponse().getContentAsString(), new TypeReference<List<com.paf.exercise.exercise.dto.Player>>() {
        });
        assertEquals(2, playerList.size());
        assertTrue(playerList.contains(new com.paf.exercise.exercise.dto.Player(1, "Mohammad")));
        assertTrue(playerList.contains(new com.paf.exercise.exercise.dto.Player(2, "Robert")));
    }

    @Test
    public void getPlayersShouldReturnNotfound() throws Exception {
        when(tournamentService.getPlayersByTournamentId(1)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/tournaments/1/players")).andExpect(status().isNotFound());
    }

    @Test
    public void removeTournamentShouldReturnNotfound() throws Exception {
        com.paf.exercise.exercise.dto.Tournament requestDto = new com.paf.exercise.exercise.dto.Tournament(null, 10, null);
        when(tournamentService.deleteTournamentById(1)).thenReturn(false);
        this.mockMvc.perform(delete("/tournaments/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isNotFound());

    }

    @Test
    public void removeTournamentShouldReturnOK() throws Exception {
        com.paf.exercise.exercise.dto.Tournament requestDto = new com.paf.exercise.exercise.dto.Tournament(null, 10, null);
        when(tournamentService.deleteTournamentById(1)).thenReturn(true);
        this.mockMvc.perform(delete("/tournaments/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isNoContent());

    }

    public static <T> T parseResponse(MvcResult result, Class<T> responseClass) {
        try {
            String contentAsString = result.getResponse().getContentAsString();
            return mapper.readValue(contentAsString, responseClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
