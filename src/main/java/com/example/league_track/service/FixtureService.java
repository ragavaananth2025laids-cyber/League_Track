package com.example.league_track.service;

import com.example.league_track.model.Fixture;
import com.example.league_track.model.Match;
import com.example.league_track.model.Team;
import com.example.league_track.repository.FixtureRepository;
import com.example.league_track.repository.MatchRepository;
import com.example.league_track.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FixtureService {

    private static final Logger log = LoggerFactory.getLogger(FixtureService.class);

    private final TeamRepository teamRepository;
    private final FixtureRepository fixtureRepository;
    private final MatchRepository matchRepository;

    public FixtureService(TeamRepository teamRepository,
                          FixtureRepository fixtureRepository,
                          MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.fixtureRepository = fixtureRepository;
        this.matchRepository = matchRepository;
    }

    /**
     * Generates a single round-robin schedule: every team plays every other
     * team once. Odd team counts are supported by assigning one bye each round.
     */
    @Transactional
    public List<Fixture> generateFixture() {
        long pending = matchRepository.countByResultRecordedFalse();
        if (pending > 0) {
            throw new IllegalStateException(
                    "Cannot reschedule while " + pending + " match(es) are still pending. Record all results first.");
        }

        List<Team> registeredTeams = teamRepository.findAll();
        if (registeredTeams.size() < 2) {
            throw new IllegalArgumentException("At least 2 teams are required");
        }

        List<Team> rotation = new ArrayList<>(registeredTeams);
        boolean odd = rotation.size() % 2 != 0;
        if (odd) {
            rotation.add(null); // BYE
        }

        int teamSlots = rotation.size();
        int rounds = teamSlots - 1;
        int matchesPerRound = teamSlots / 2;
        List<Fixture> fixtures = new ArrayList<>();

        for (int round = 1; round <= rounds; round++) {
            Fixture fixture = fixtureRepository.save(new Fixture(round));
            fixtures.add(fixture);

            for (int i = 0; i < matchesPerRound; i++) {
                Team home = rotation.get(i);
                Team away = rotation.get(teamSlots - 1 - i);

                // A null slot represents the bye; no match is created.
                if (home == null || away == null) {
                    continue;
                }

                Match match = new Match();
                match.setFixture(fixture);
                match.setHomeTeam(home);
                match.setAwayTeam(away);
                matchRepository.save(match);
            }

            // Keep the first team fixed; rotate all remaining slots clockwise.
            List<Team> next = new ArrayList<>();
            next.add(rotation.get(0));
            next.add(rotation.get(teamSlots - 1));
            next.addAll(rotation.subList(1, teamSlots - 1));
            rotation = next;
        }

        log.info("Successfully generated {} round-robin fixture rounds for {} teams", fixtures.size(), registeredTeams.size());
        return fixtures;
    }

    @Transactional(readOnly = true)
    public List<Fixture> getFixtures() {
        return fixtureRepository.findAllByOrderByRoundNumberAsc();
    }

    @Transactional(readOnly = true)
    public List<Match> getMatchesByFixtureId(Long fixtureId) {
        return matchRepository.findByFixtureIdOrderByIdAsc(fixtureId);
    }

    /**
     * Returns true when at least one fixture has been played and every
     * scheduled match has a recorded result — i.e. the league is complete
     * and a new fixture can be generated.
     */
    @Transactional(readOnly = true)
    public boolean isLeagueComplete() {
        return matchRepository.count() > 0 && matchRepository.countByResultRecordedFalse() == 0;
    }

    /** How many matches still need a result recorded. */
    @Transactional(readOnly = true)
    public long getPendingMatchCount() {
        return matchRepository.countByResultRecordedFalse();
    }
}
