package com.example.pac.pacman;

import android.content.res.Resources;

import java.util.Arrays;
import java.util.Collection;

public class GhostRepository {
    public static Collection<Ghost> CreateGhosts(Resources resources, Labyrinth labyrinth) {
        return Arrays.asList(
                new Ghost("Shadow", "Blinky", resources.getColor(R.color.Blinky), new DummyRandomMoveStrategy(labyrinth), labyrinth),
                new Ghost("Bashful", "Inky", resources.getColor(R.color.Inky), new DummyRandomMoveStrategy(labyrinth), labyrinth),
                new Ghost("Speedy", "Pinky", resources.getColor(R.color.Pinky), new DummyRandomMoveStrategy(labyrinth), labyrinth),
                new Ghost("Pokey", "Clyde", resources.getColor(R.color.Clyde), new DummyRandomMoveStrategy(labyrinth), labyrinth));
    }
}
