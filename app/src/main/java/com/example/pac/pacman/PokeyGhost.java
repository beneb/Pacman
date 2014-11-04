package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Color;

public class PokeyGhost extends Ghost {

    public PokeyGhost(Resources resources, Labyrinth labyrinth) {
        super(resources, labyrinth, Color.rgb(255, 102, 0), "Pokey", "Clyde");
    }
}
