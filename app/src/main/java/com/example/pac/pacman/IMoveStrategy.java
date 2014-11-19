package com.example.pac.pacman;

public interface IMoveStrategy {
    Direction GetCurrentOrNextDirection(float x, float y);
}

