package com.example.pac.pacman;

public class StopMoveStrategy implements IMoveStrategy {
    @Override
    public Direction getNextDirection(int currentCell) {
        return Direction.Stopped;
    }
}
