package com.example.pac.pacman.activities;

import android.os.Handler;

public class FrameLoop {
    private final static int FPS = 30;

    private final Handler _handler = new Handler();
    private final GameLogic _gameLogic;
    private boolean _stopped = false;

    public FrameLoop(GameLogic gameLogic) {
        _gameLogic = gameLogic;
    }

    public void start() {
        _stopped = false;
        _handler.postDelayed(_runnable, 500);
    }

    public void stop() {
        _stopped = true;
    }

    private Runnable _runnable = new Runnable() {
        public void run() {
            _gameLogic.UpdateOnFrame();
            _handler.removeCallbacks(_runnable);
            if (!_stopped) {
                _handler.postDelayed(this, 1000 / FPS);
            }
        }
    };
}
