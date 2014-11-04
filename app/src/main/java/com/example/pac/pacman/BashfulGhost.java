package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Color;

public class BashfulGhost extends Ghost {

    public BashfulGhost(Resources resources, Labyrinth labyrinth) {
        super(resources, labyrinth, Color.CYAN, "Bashful", "Inky", new DummyRandomMoveStrategy());
    }
}
