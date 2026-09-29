package com.example.league_track.service;

import com.example.league_track.model.Match;
import com.example.league_track.model.StandingsEntry;
import com.example.league_track.repository.MatchRepository;
import com.example.league_track.repository.StandingsEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchService {

    private static final Logger log = LoggerFactory.getLogger(MatchService.class);

    private final MatchRepository matchRepository;
    private final StandingsEntryRepository standingsEntryRepository;

    @Value("${league.points.win:3}")
    private int winPoints;

    @Value("${league.points.draw:1}")
    private int drawPoints;

    @Value("${league.points.loss:0}")
    private int lossPoints;

    public MatchService(MatchRepository matchRepository,
                         StandingsEntryRepository standingsEntryRepository) {
        this.matchRepository = matchRepository;
        this.standingsEntryRepository = standingsEntryRepository;
    }

    @Transactional
    public Match recordResult(Long matchId, int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative");
        }

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        if (match.isResultRecorded()) {
            throw new IllegalStateException("Result has already been recorded for this match");
        }

        StandingsEntry home = standingsEntryRepository.findByTeamId(match.getHomeTeam().getId())
                .orElseThrow(() -> new IllegalStateException("Home team standings not found"));
        StandingsEntry away = standingsEntryRepository.findByTeamId(match.getAwayTeam().getId())
                .orElseThrow(() -> new IllegalStateException("Away team standings not found"));

        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);

        if (homeScore > awayScore) {
            home.addWin(winPoints);
            away.addLoss(lossPoints);
        } else if (homeScore < awayScore) {
            home.addLoss(lossPoints);
            away.addWin(winPoints);
        } else {
            home.addDraw(drawPoints);
            away.addDraw(drawPoints);
        }

        standingsEntryRepository.save(home);
        standingsEntryRepository.save(away);

        match.setResultRecorded(true);
        Match savedMatch = matchRepository.save(match);
        log.info("Recorded Match {} result: {} {} - {} {}. Standings table updated.",
                match.getId(), match.getHomeTeam().getName(), homeScore, awayScore, match.getAwayTeam().getName());
        return savedMatch;
    }

    @Transactional(readOnly = true)
    public Match getMatch(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + id));
    }

    @Transactional(readOnly = true)
    public java.util.List<Match> getAllMatches() {
        return matchRepository.findAllByOrderByIdAsc();
    }
}
