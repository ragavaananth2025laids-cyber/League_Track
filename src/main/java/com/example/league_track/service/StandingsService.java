package com.example.league_track.service;

import com.example.league_track.model.StandingsEntry;
import com.example.league_track.repository.StandingsEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StandingsService {

    private final StandingsEntryRepository standingsEntryRepository;

    public StandingsService(StandingsEntryRepository standingsEntryRepository) {
        this.standingsEntryRepository = standingsEntryRepository;
    }

    @Transactional(readOnly = true)
    public List<StandingsEntry> getStandings() {
        return standingsEntryRepository.findAllByOrderByPointsDescWinsDesc();
    }
}
