package com.example.pac.pacman;

import android.content.res.Resources;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Viktor on 04.11.2014.
 */
public class GhostRepository {
    public static Collection<Ghost> CreateGhosts(Resources resources, Labyrinth labyrinth) {
        return Arrays.asList(
                new ShadowGhost(resources, labyrinth),
                new BashfulGhost(resources, labyrinth),
                new SpeedyGhost(resources, labyrinth),
                new PokeyGhost(resources, labyrinth));
    }
}
