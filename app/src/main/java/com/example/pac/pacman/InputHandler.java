package com.example.pac.pacman;

import android.graphics.PointF;

import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.EventManager;
import com.example.pac.pacman.event.PacManDirectionRequested;

public class InputHandler {

    private PacMan _pacMan;
    private PacManMoveStrategy _pacManStrategy;

    public InputHandler(PacMan pacMan, IMoveStrategy pacManMoveStrategy) {
        _pacMan = pacMan;
        _pacManStrategy = (PacManMoveStrategy) pacManMoveStrategy;
    }

    public EventListener<PacManDirectionRequested> DirectionChangedListener = new EventListener<PacManDirectionRequested>() {
        @Override
        public void onEvent(PacManDirectionRequested event) {

            PointF curPos = _pacMan.getPosition();
            if (Math.abs(event.getNewX() - curPos.x) > Math.abs(event.getNewY() - curPos.y)) { // horizontal move
                if (event.getNewX() < curPos.x) {
                    _pacManStrategy.setWishDirection(Direction.Left);
                } else {
                    _pacManStrategy.setWishDirection(Direction.Right);
                }
            } else { // vertical move
                if (event.getNewY() < curPos.y) {
                    _pacManStrategy.setWishDirection(Direction.Up);
                } else {
                    _pacManStrategy.setWishDirection(Direction.Down);
                }
            }
        }
    };
}
