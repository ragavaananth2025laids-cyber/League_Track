package com.example.league_track.controller;

import com.example.league_track.model.Match;
import com.example.league_track.service.MatchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/{matchId}/result")
    public Match recordResult(@PathVariable Long matchId,
                               @RequestParam int homeScore,
                               @RequestParam int awayScore) {
        return matchService.recordResult(matchId, homeScore, awayScore);
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping("/{matchId}")
    public Match getMatch(@PathVariable Long matchId) {
        return matchService.getMatch(matchId);
    }
}
