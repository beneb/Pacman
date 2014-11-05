package com.example.pac.pacman;

import android.graphics.Color;

public class PokeyGhost extends Ghost {

    public PokeyGhost() {
        super(Color.rgb(255, 102, 0), "Pokey", "Clyde", new DummyRandomMoveStrategy());
    }
}