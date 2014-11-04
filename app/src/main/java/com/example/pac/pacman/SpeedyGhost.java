package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Color;

public class SpeedyGhost extends Ghost {

    public SpeedyGhost(Resources resources, Labyrinth labyrinth) {
        super(resources, labyrinth, Color.MAGENTA, "Speedy", "Pinky", new DummyRandomMoveStrategy(labyrinth));
    }
}
