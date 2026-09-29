package com.example.league_track.controller;

import com.example.league_track.model.StandingsEntry;
import com.example.league_track.service.StandingsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/standings")
public class StandingsController {

    private final StandingsService standingsService;

    public StandingsController(StandingsService standingsService) {
        this.standingsService = standingsService;
    }

    @GetMapping
    public List<StandingsEntry> getStandings() {
        return standingsService.getStandings();
    }
}
