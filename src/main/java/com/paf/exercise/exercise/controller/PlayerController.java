package com.paf.exercise.exercise.controller;

import com.paf.exercise.exercise.dto.Player;
import com.paf.exercise.exercise.service.PlayerService;
import com.paf.exercise.exercise.util.mapper.PlayerMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@Api(tags = {"Players"})
@SwaggerDefinition(tags = {
        @Tag(name = "Players", description = "Operations related to players")
})
@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;
    private final PlayerMapper playerMapper = Mappers.getMapper(PlayerMapper.class);

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @ApiOperation(value = "Create a new player", response = Player.class)
    @PostMapping("/")
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        return ResponseEntity.ok(playerMapper.toPlayerDto(playerService.addPlayer(player)));
    }
}
