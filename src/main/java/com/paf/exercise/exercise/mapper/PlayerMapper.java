package com.paf.exercise.exercise.mapper;

import com.paf.exercise.exercise.entity.Player;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@Mapper
public abstract class PlayerMapper {
    public abstract List<com.paf.exercise.exercise.dto.Player> toPlayerDto(List<Player> playerList);

    public abstract com.paf.exercise.exercise.dto.Player toPlayerDto(Player player);

}
