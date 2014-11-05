package com.example.pac.pacman;

import android.graphics.Color;

public class ShadowGhost extends Ghost {

    public ShadowGhost() {
        super(Color.RED, "Shadow", "Blinky", new DummyRandomMoveStrategy());
    }
}
