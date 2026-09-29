package com.example.league_track.model;

import jakarta.persistence.*;

@Entity
@Table(name = "standings")
public class StandingsEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "team_id", nullable = false, unique = true)
    private Team team;

    @Column(nullable = false)
    private int matchesPlayed;

    @Column(nullable = false)
    private int wins;

    @Column(nullable = false)
    private int draws;

    @Column(nullable = false)
    private int losses;

    @Column(nullable = false)
    private int points;

    public StandingsEntry() {
    }

    public StandingsEntry(Team team) {
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public int getPoints() {
        return points;
    }

    public void addWin(int winPoints) {
        matchesPlayed++;
        wins++;
        points += winPoints;
    }

    public void addDraw(int drawPoints) {
        matchesPlayed++;
        draws++;
        points += drawPoints;
    }

    public void addLoss(int lossPoints) {
        matchesPlayed++;
        losses++;
        points += lossPoints;
    }
}
