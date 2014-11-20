package com.example.pac.pacman.event;

public class ChangeHitPointsEvent {
    private boolean _incHitPoint;

    public ChangeHitPointsEvent(boolean incHitPoint) {
        _incHitPoint = incHitPoint;
    }

    public boolean IncreaseHitPoints() {
        return _incHitPoint;
    }
}
