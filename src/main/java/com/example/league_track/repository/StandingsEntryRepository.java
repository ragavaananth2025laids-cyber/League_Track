package com.example.league_track.repository;

import com.example.league_track.model.StandingsEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StandingsEntryRepository extends JpaRepository<StandingsEntry, Long> {
    Optional<StandingsEntry> findByTeamId(Long teamId);
    List<StandingsEntry> findAllByOrderByPointsDescWinsDesc();
}
