package com.example.pac.pacman;

import android.content.res.Resources;

import java.util.Arrays;
import java.util.Collection;

public class GhostRepository {
    public static Collection<Ghost> CreateGhosts(Resources resources, Labyrinth labyrinth) {
        return Arrays.asList(
                new Ghost(resources.getColor(R.color.Blinky), new DummyRandomMoveStrategy(labyrinth), labyrinth) {
                    @Override
                    public String getName() {
                        return "Shadow";
                    }

                    @Override
                    public String getNickName() {
                        return "Blinky";
                    }

                    @Override
                    public char getId() {
                        return 'b';
                    }
                },
                new Ghost(resources.getColor(R.color.Inky), new DummyRandomMoveStrategy(labyrinth), labyrinth) {
                    @Override
                    public String getName() {
                        return "Bashful";
                    }

                    @Override
                    public String getNickName() {
                        return "Inky";
                    }

                    @Override
                    public char getId() {
                        return 'i';
                    }
                },
                new Ghost(resources.getColor(R.color.Pinky), new DummyRandomMoveStrategy(labyrinth), labyrinth) {
                    @Override
                    public String getName() {
                        return "Speedy";
                    }

                    @Override
                    public String getNickName() {
                        return "Pinky";
                    }

                    @Override
                    public char getId() {
                        return 'p';
                    }
                },
                new Ghost(resources.getColor(R.color.Clyde), new DummyRandomMoveStrategy(labyrinth), labyrinth) {
                    @Override
                    public String getName() {
                        return "Pokey";
                    }

                    @Override
                    public String getNickName() {
                        return "Clyde";
                    }

                    @Override
                    public char getId() {
                        return 'c';
                    }
                });
    }
}
