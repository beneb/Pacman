package com.example.pac.pacman.event;

import com.example.pac.pacman.Ghost;

public class GhostEatenEvent extends ScoreEvent {

    private Ghost _ghostWasEaten;

    public GhostEatenEvent(Ghost ghost, int score) {
        super(score);
        _ghostWasEaten = ghost;
    }

    public Ghost GetGhostWasEaten() {
        return _ghostWasEaten;
    }
}

