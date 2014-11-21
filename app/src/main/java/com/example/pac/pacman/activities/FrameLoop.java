package com.example.pac.pacman.activities;

import android.os.Handler;

import com.example.pac.pacman.GameLogicHandler;

public class FrameLoop {
    private final static int FPS = 30;

    private final Handler _handler = new Handler();
    private final GameLogicHandler _gameLogic;

    public FrameLoop(GameLogicHandler gameLogic) {
        _gameLogic = gameLogic;
    }

    public void Start() {
        _handler.postDelayed(FrameLoop, 1000);
    }

    public void Destroy() {
        _handler.removeCallbacks(FrameLoop);
    }

    private Runnable FrameLoop = new Runnable() {
        public void run() {
            _gameLogic.UpdateOnFrame();

            _handler.removeCallbacks(FrameLoop);
            _handler.postDelayed(this, 1000 / FPS);
        }
    };
}
