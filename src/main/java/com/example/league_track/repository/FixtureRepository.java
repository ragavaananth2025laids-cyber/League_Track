package com.example.league_track.repository;

import com.example.league_track.model.Fixture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FixtureRepository extends JpaRepository<Fixture, Long> {
    List<Fixture> findAllByOrderByRoundNumberAsc();
}
