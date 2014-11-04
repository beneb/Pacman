package com.example.pac.pacman;

import java.util.ArrayList;

public class RandomStrategy implements IMoveStrategy {
    private final Labyrinth _labyrinth;
    private Labyrinth.Cell _lastCell;
    private Direction _lastDirection;

    public RandomStrategy(Labyrinth labyrinth) {
        _labyrinth = labyrinth;
    }

    @Override
    public Direction getNextDirection(float x, float y) {
        Labyrinth.Cell cell = _labyrinth.cellAt(x, y);
        if (!cell.equals(_lastCell)) {
            _lastCell = cell;
            ArrayList<Direction> directions = new ArrayList<Direction>();
            if (_labyrinth.canMove(cell, Direction.Left)) {
                directions.add(Direction.Left);
            }
            if (_labyrinth.canMove(cell, Direction.Up)) {
                directions.add(Direction.Up);
            }
            if (_labyrinth.canMove(cell, Direction.Right)) {
                directions.add(Direction.Right);
            }
            if (_labyrinth.canMove(cell, Direction.Down)) {
                directions.add(Direction.Down);
            }
            double fraction = 1.0 / directions.size();
            double rnd = Math.random();
            _lastDirection = directions.get((int) (rnd / fraction));
        }
        return _lastDirection;
    }
}