package com.example.pac.pacman;

public final class GameEnv {

    private static GameEnv instance;
    private GameEnv() {}

    private Labyrinth _labyrinth;
    private android.content.res.Resources _resources;

    public void InitOnce(android.content.res.Resources resources) {
        if (_labyrinth == null) {
            _labyrinth = new Labyrinth(resources);
        }

        if (_resources == null) {
            _resources = resources;
        }
    }

    public static GameEnv getInstance() {
        if (GameEnv.instance == null) {
            GameEnv.instance = new GameEnv();
        }
        return GameEnv.instance;
    }

    public Labyrinth getLabyrinth() {
        return _labyrinth;
    }

    public android.content.res.Resources getResources() {
        return _resources;
    }
}
