package com.example.football.models.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team extends BaseEntity {
    private String name;
    private String stadiumName;
    private int fanBase;
    private String history;
    private Town town;
    private Set<Player> players;

    public Team() {
    }

    @Column(name = "name", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "stadium_name", nullable = false)
    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    @Column(name = "fan_base", nullable = false)
    public int getFanBase() {
        return fanBase;
    }

    public void setFanBase(int fanBase) {
        this.fanBase = fanBase;
    }

    @Column(name = "history", columnDefinition = "TEXT", nullable = false)
    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    @ManyToOne
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    @OneToMany(mappedBy = "team")
    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}
