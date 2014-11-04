package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Color;

public class ShadowGhost extends Ghost {

    public ShadowGhost(Resources resources, Labyrinth labyrinth) {
        super(resources, labyrinth, Color.RED, "Shadow", "Blinky");
    }
}
