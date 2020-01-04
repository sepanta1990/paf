package com.paf.exercise.exercise.mapper;

import com.paf.exercise.exercise.entity.Tournament;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@Mapper
public abstract class TournamentMapper {
    public abstract List<com.paf.exercise.exercise.dto.Tournament> toTournamentDto(List<Tournament> titleList);

    public abstract com.paf.exercise.exercise.dto.Tournament toTournamentDto(Tournament tournament);

}
