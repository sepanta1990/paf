package com.paf.exercise.exercise.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tournament {
    private Integer id;
    private Integer rewardAmount;
    private HashSet<Player> players = new HashSet<>();
}
