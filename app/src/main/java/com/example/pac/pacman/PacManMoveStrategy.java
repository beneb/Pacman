package com.example.pac.pacman;

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
    public Direction GetCurrentOrNextDirection(float x, float y) {
        int cell = _labyrinth.cellAt(x, y);
        if (_labyrinth.canMove(cell, _wishDirection)) {
            _direction = _wishDirection;
        } else if (!_labyrinth.canMove(cell, _direction)) {
            _direction = Direction.Stopped;
        }
        return _direction;
    }
}
