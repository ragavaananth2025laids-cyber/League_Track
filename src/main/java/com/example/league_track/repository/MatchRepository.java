package com.example.league_track.repository;

import com.example.league_track.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByFixtureIdOrderByIdAsc(Long fixtureId);
    List<Match> findAllByOrderByIdAsc();
    long countByResultRecordedFalse();
}
