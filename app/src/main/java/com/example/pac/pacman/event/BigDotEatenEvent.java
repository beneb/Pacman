package com.example.pac.pacman.event;

public class BigDotEatenEvent {
    private int _cell;

    public BigDotEatenEvent(int cell) {
        _cell = cell;
    }

    public int GetCell() {
        return _cell;
    }
}

