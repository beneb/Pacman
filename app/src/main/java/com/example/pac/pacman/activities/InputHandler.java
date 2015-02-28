package com.example.pac.pacman.activities;

import android.graphics.PointF;

import com.example.pac.pacman.Direction;
import com.example.pac.pacman.IMoveStrategy;
import com.example.pac.pacman.PacMan;
import com.example.pac.pacman.PacManMoveStrategy;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.PacManDirectionRequestEvent;

public class InputHandler {

    private PacMan _pacMan;

    public InputHandler(PacMan pacMan) {
        _pacMan = pacMan;
    }

    public EventListener<PacManDirectionRequestEvent> DirectionChangedListener = new EventListener<PacManDirectionRequestEvent>() {
        @Override
        public void onEvent(PacManDirectionRequestEvent event) {
            PacManMoveStrategy pacManStrategy = (PacManMoveStrategy)_pacMan.getMoveStrategy();
            PointF curPos = _pacMan.getPosition();
            if (Math.abs(event.getNewX() - curPos.x) > Math.abs(event.getNewY() - curPos.y)) { // horizontal move
                if (event.getNewX() < curPos.x) {
                    pacManStrategy.setWishDirection(Direction.Left);
                } else {
                    pacManStrategy.setWishDirection(Direction.Right);
                }
            } else { // vertical move
                if (event.getNewY() < curPos.y) {
                    pacManStrategy.setWishDirection(Direction.Up);
                } else {
                    pacManStrategy.setWishDirection(Direction.Down);
                }
            }
        }
    };
}
