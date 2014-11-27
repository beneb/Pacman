package com.example.pac.pacman.activities;

import android.os.Handler;

import com.example.pac.pacman.GameLogicHandler;

public class FrameLoop {
    private final static int FPS = 30;

    private final Handler _handler = new Handler();
    private final GameLogicHandler _gameLogic;
    private boolean _stopped = false;

    public FrameLoop(GameLogicHandler gameLogic) {
        _gameLogic = gameLogic;
    }

    public void start() {
        _stopped = false;
        _handler.postDelayed(FrameLoop, 500);
    }

    public void stop() {
        _stopped = true;
    }

    private Runnable FrameLoop = new Runnable() {
        public void run() {
            _gameLogic.UpdateOnFrame();

            _handler.removeCallbacks(FrameLoop);
            if (!_stopped) {
                _handler.postDelayed(this, 1000 / FPS);
            }
        }
    };
}
