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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

}
