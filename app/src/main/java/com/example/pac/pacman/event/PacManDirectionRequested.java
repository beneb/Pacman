package com.example.pac.pacman.event;

public class PacManDirectionRequested {
    private float _x, _y;

    public PacManDirectionRequested(float newX, float newY) {
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
