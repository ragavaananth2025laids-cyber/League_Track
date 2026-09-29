package com.example.league_track.service;

import com.example.league_track.model.StandingsEntry;
import com.example.league_track.model.Team;
import com.example.league_track.repository.StandingsEntryRepository;
import com.example.league_track.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;
    private final StandingsEntryRepository standingsEntryRepository;

    public TeamService(TeamRepository teamRepository,
                       StandingsEntryRepository standingsEntryRepository) {
        this.teamRepository = teamRepository;
        this.standingsEntryRepository = standingsEntryRepository;
    }

    @Transactional
    public Team registerTeam(Team team) {
        if (team.getName() == null || team.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Team name is required");
        }

        String cleanName = team.getName().trim();
        if (teamRepository.findByNameIgnoreCase(cleanName).isPresent()) {
            throw new IllegalArgumentException("Team already exists: " + cleanName);
        }

        team.setName(cleanName);
        Team savedTeam = teamRepository.save(team);
        standingsEntryRepository.save(new StandingsEntry(savedTeam));
        log.info("Registered new team: '{}' (ID: {}) and initialized standings", savedTeam.getName(), savedTeam.getId());
        return savedTeam;
    }

    @Transactional(readOnly = true)
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
}
