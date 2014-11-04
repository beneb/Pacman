package com.example.pac.pacman;

/**
 * Created by Viktor on 04.11.2014.
 */
public abstract class MoveStrategy {
    public abstract Character.Direction GetNextDirection(Ghost performer);
}
