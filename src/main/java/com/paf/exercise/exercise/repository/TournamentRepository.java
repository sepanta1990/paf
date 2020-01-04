package com.paf.exercise.exercise.repository;

import com.paf.exercise.exercise.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Mohammad Fathizadeh 2020-01-04
 */
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
}
