package com.example.pac.pacman;

import java.util.Arrays;
import java.util.Collection;

public class GhostRepository {
    public static Collection<Ghost> CreateGhosts() {
        return Arrays.asList(
                new ShadowGhost(),
                new BashfulGhost(),
                new SpeedyGhost(),
                new PokeyGhost());
    }
}
