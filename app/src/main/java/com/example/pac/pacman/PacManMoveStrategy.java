package com.example.pac.pacman;

import android.graphics.PointF;

public class PacManMoveStrategy implements IMoveStrategy {

    public PacManMoveStrategy(Labyrinth _labyrinth) {
        this._labyrinth = _labyrinth;
    }

    private final Labyrinth _labyrinth;

    private Direction _wishDirection = Direction.Stopped;
    private Direction _direction = Direction.Stopped;

    public void setWishDirection(Direction direction) {
        _wishDirection = direction;
    }

    @Override
    public Direction GetNextDirection(int cell) {
        if (_labyrinth.canMove(cell, _wishDirection)) {
            _direction = _wishDirection;
        } else if (!_labyrinth.canMove(cell, _direction)) {
            _direction = Direction.Stopped;
        }
        return _direction;
    }
}
