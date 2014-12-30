package com.example.pac.pacman.event;

public class ScoreEvent {
    private int _score;

    public ScoreEvent(int score) {
        _score = score;
    }

    public int GetScore() {
        return _score;
    }
}
