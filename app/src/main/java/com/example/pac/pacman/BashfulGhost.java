package com.example.pac.pacman;

import android.graphics.Color;

public class BashfulGhost extends Ghost {

    public BashfulGhost() {
        super(Color.CYAN, "Bashful", "Inky", new DummyRandomMoveStrategy());
    }
}
