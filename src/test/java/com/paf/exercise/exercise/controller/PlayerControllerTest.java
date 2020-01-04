package com.paf.exercise.exercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paf.exercise.exercise.entity.Player;
import com.paf.exercise.exercise.service.PlayerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@RunWith(SpringRunner.class)
@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;


    @Test
    public void addPlayerShouldReturnOK() throws Exception {

        com.paf.exercise.exercise.dto.Player requestDto = new com.paf.exercise.exercise.dto.Player(null, "Mohammad");
        when(playerService.addPlayer(requestDto)).thenReturn(new Player(2, "Mohammad", null));
        MvcResult requestResult = this.mockMvc.perform(post("/players/").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk()).andReturn();

        com.paf.exercise.exercise.dto.Player responseDto = parseResponse(requestResult, com.paf.exercise.exercise.dto.Player.class);
        assertEquals(responseDto.getName(), requestDto.getName());
        assertEquals(responseDto.getId(), new Integer(2));
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
