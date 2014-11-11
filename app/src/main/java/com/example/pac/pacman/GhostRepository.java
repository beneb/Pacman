package com.example.pac.pacman;

import android.graphics.Color;

import java.util.Arrays;
import java.util.Collection;

public class GhostRepository {
    public static Collection<Ghost> CreateGhosts() {
        Labyrinth lab = GameEnv.getInstance().getLabyrinth();
        return Arrays.asList(
                new Ghost(Color.RED, "Shadow", "Blinky", new DummyRandomMoveStrategy(), lab),
                new Ghost(Color.CYAN, "Bashful", "Inky", new DummyRandomMoveStrategy(), lab),
                new Ghost(Color.MAGENTA, "Speedy", "Pinky", new DummyRandomMoveStrategy(), lab),
                new Ghost(Color.rgb(255, 102, 0), "Pokey", "Clyde", new DummyRandomMoveStrategy(), lab)).subList(0, 1);
    }
}
