package com.example.pac.pacman;

import android.graphics.PointF;
import android.graphics.RectF;

public class PacManEnvironment {
    private final Labyrinth _labyrinth;
    private final PacMan _pacMan;
    private final PacManMoveStrategy _moveStrategy;
    public Labyrinth getLabyrinth() {
        return _labyrinth;
    }
    public PacManMoveStrategy getMoveStrategy() {
        return _moveStrategy;
    }

    PacManEnvironment(String state, float width, float height) {
        _labyrinth = new Labyrinth(state, null);
        _labyrinth.init(new RectF(0, 0, width, height));
        _moveStrategy = new PacManMoveStrategy(_labyrinth);
        _pacMan = new PacMan(0, _moveStrategy, _labyrinth);
        _pacMan.init();
    }

    public void moveToTheNextCell() {
        int cell = _labyrinth.getCharacterPosition(_pacMan);
        for (int i = 0; i < 10; i++) {
            _pacMan.move();
            if (_labyrinth.getCharacterPosition(_pacMan) != cell) {
                break;
            }
        }
    }

    public void movePacManToTheWall() {
        for (int i = 0; i < 10; i++) {
            _pacMan.move();
        }
    }

    public int getPacManPosition() {
        return _labyrinth.getCharacterPosition(_pacMan);
    }

    public PointF getPacManCoordinates() {
        return _pacMan.getPosition();
    }
}
