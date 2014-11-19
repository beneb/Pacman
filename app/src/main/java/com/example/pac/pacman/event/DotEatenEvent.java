package com.example.pac.pacman.event;
public class DotEatenEvent {
    private int _cell;

    public DotEatenEvent(int cell) {
        _cell = cell;
    }

    public int GetCell() {
        return _cell;
    }
}

