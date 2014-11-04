package com.example.pac.pacman;

public interface IMoveStrategy {
    Direction getNextDirection(float x, float y);
}

enum Direction {
    Stopped,
    Left,
    Right,
    Up,
    Down;
}