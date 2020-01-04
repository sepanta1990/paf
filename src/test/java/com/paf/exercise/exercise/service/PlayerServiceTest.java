package com.paf.exercise.exercise.service;


import com.paf.exercise.exercise.entity.Player;
import com.paf.exercise.exercise.repository.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PlayerServiceTest {
    @MockBean
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Test
    public void addPlayerTest() {
        Player playerToSave = new Player(null, "Mohammad", null);
        Player savedPlayer = new Player(1, "Mohammad", null);

        when(playerRepository.save(playerToSave)).thenReturn(savedPlayer);

        assertEquals(playerService.addPlayer(new com.paf.exercise.exercise.dto.Player(null, "Mohammad")), savedPlayer);

    }
}
