package com.example.pac.pacman;

import android.graphics.Color;

public class SpeedyGhost extends Ghost {

    public SpeedyGhost() {
        super(Color.MAGENTA, "Speedy", "Pinky", new DummyRandomMoveStrategy());
    }
}
