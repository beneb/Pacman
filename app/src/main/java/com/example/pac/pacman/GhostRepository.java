package com.example.pac.pacman;

import android.content.res.Resources;
import android.support.v4.util.ArrayMap;

import com.example.pac.pacman.views.GhostView;
import com.example.pac.pacman.views.IChildView;

import java.util.Map;

public class GhostRepository {
    public static Map<Ghost, IChildView> CreateGhosts(final Resources resources, Labyrinth labyrinth) {
        Ghost blinky =
                new Ghost(new RandomMoveStrategy(labyrinth), labyrinth) {
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
                };
        Ghost inky =
                new Ghost(new RandomMoveStrategy(labyrinth), labyrinth) {
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
                };
        Ghost pinky =
                new Ghost(new RandomMoveStrategy(labyrinth), labyrinth) {
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
                };
        Ghost clyde =
                new Ghost(new RandomMoveStrategy(labyrinth), labyrinth) {
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
                };

        Map<Ghost, IChildView> ghostsMitViews = new ArrayMap<Ghost, IChildView>();
        ghostsMitViews.put(blinky, new GhostView(blinky, resources) {
            {
                _defaultColor = resources.getColor(R.color.Blinky);
            }
        });
        ghostsMitViews.put(inky, new GhostView(inky, resources) {
            {
                _defaultColor = resources.getColor(R.color.Inky);
            }
        });
        ghostsMitViews.put(pinky, new GhostView(pinky, resources) {
            {
                _defaultColor = resources.getColor(R.color.Pinky);
            }
        });
        ghostsMitViews.put(clyde, new GhostView(clyde, resources) {
            {
                _defaultColor = resources.getColor(R.color.Clyde);
            }
        });

        return ghostsMitViews;
    }
}
