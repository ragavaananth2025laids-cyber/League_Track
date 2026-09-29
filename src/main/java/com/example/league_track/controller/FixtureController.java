package com.example.league_track.controller;

import com.example.league_track.model.Fixture;
import com.example.league_track.model.Match;
import com.example.league_track.service.FixtureService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fixtures")
public class FixtureController {

    private final FixtureService fixtureService;

    public FixtureController(FixtureService fixtureService) {
        this.fixtureService = fixtureService;
    }

    /**
     * Generate (or re-generate) a round-robin fixture.
     * Re-generation is only allowed once ALL existing matches have a recorded result.
     * Returns 201 Created on success.
     */
    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Fixture> generateFixture() {
        return fixtureService.generateFixture();
    }

    /** Returns all fixtures ordered by round number. */
    @GetMapping
    public List<Fixture> getFixtures() {
        return fixtureService.getFixtures();
    }

    /** Returns all matches belonging to a specific fixture/round. */
    @GetMapping("/{fixtureId}/matches")
    public List<Match> getMatches(@PathVariable Long fixtureId) {
        return fixtureService.getMatchesByFixtureId(fixtureId);
    }

    /**
     * Returns the current league status:
     *   - leagueComplete   : true when every scheduled match has a result
     *   - pendingMatches   : number of matches still awaiting a result
     *   - canReschedule    : true when a new fixture can be generated
     */
    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        boolean complete = fixtureService.isLeagueComplete();
        long pending = fixtureService.getPendingMatchCount();
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("leagueComplete", complete);
        status.put("pendingMatches", pending);
        status.put("canReschedule", pending == 0);   // true even before first fixture
        return status;
    }
}
