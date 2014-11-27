package com.example.pac.pacman.event;

public class PacManDirectionRequestEvent {
    private float _x, _y;

    public PacManDirectionRequestEvent(float newX, float newY) {
        _x = newX;
        _y = newY;
    }

    public float getNewX() {
        return _x;
    }

    public float getNewY() {
        return _y;
    }
}
