package com.example.pac.pacman;

public interface IMoveStrategy {
    Direction getNextDirection(int currentCell);
}

