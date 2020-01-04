package com.paf.exercise.exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer rewardAmount;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "tournament_player", joinColumns = {
            @JoinColumn(name = "tournament_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "player_id",
                    nullable = false, updatable = false)})
    private Set<Player> players;
}
