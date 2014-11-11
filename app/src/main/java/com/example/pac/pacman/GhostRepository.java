package com.example.pac.pacman;

import android.graphics.Color;

import java.util.Arrays;
import java.util.Collection;

public class GhostRepository {
    public static Collection<Ghost> CreateGhosts(Labyrinth labyrinth) {
        return Arrays.asList(
                new Ghost(Color.RED, "Shadow", "Blinky", labyrinth, new DummyRandomMoveStrategy(labyrinth)),
                new Ghost(Color.CYAN, "Bashful", "Inky", labyrinth, new DummyRandomMoveStrategy(labyrinth)),
                new Ghost(Color.MAGENTA, "Speedy", "Pinky", labyrinth, new DummyRandomMoveStrategy(labyrinth)),
                new Ghost(Color.rgb(255, 102, 0), "Pokey", "Clyde", labyrinth, new DummyRandomMoveStrategy(labyrinth)));
    }
}
