package com.example.pac.pacman;

public class StopMoveStrategy implements IMoveStrategy {
    @Override
    public Direction GetNextDirection(int currentCell) {
        return Direction.Stopped;
    }
}
