package com.example.pac.pacman.event;

public class ChangeLifesEvent {
    private boolean _oneUp;

    public ChangeLifesEvent(boolean oneUp) {
        _oneUp = oneUp;
    }

    public boolean OneUp() {
        return _oneUp;
    }
}
