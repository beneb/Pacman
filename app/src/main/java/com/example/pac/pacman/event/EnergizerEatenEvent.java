package com.example.pac.pacman.event;

public class EnergizerEatenEvent {
    private int _cell;

    public EnergizerEatenEvent(int cell) {
        _cell = cell;
    }

    public int GetCell() {
        return _cell;
    }
}

