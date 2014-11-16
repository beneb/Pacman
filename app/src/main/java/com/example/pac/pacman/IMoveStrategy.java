package com.example.pac.pacman;

public interface IMoveStrategy {
    Direction GetCurrentOrNextDirection(float x, float y);
}

enum Direction {
    Stopped,
    Left,
    Right,
    Up,
    Down;

    public boolean isPerpendicular(Direction direction) {
        switch (this) {
            case Stopped:
                return false;
            case Left:
            case Right:
                return direction == Up || direction == Down;
            case Up:
            case Down:
                return direction == Left || direction == Right;
        }
        return false;
    }
}