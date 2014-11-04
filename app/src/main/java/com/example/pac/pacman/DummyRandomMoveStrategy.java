package com.example.pac.pacman;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by Viktor on 04.11.2014.
 */
public class DummyRandomMoveStrategy implements IMoveStrategy {
    private final Labyrinth _labyrinth;
    private Labyrinth.Cell _lastCell;
    private Direction _lastDirection;

    public DummyRandomMoveStrategy(Labyrinth labyrinth) {
        _labyrinth = labyrinth;
    }

    @Override
    public Direction getNextDirection(float x, float y) {
        Labyrinth.Cell cell = _labyrinth.cellAt(x, y);
        if (!cell.equals(_lastCell)) {
            _lastCell = cell;
            ArrayList<Direction> directions = new ArrayList<Direction>();
            for (Direction direction : Direction.values()) {
                if (_labyrinth.canMove(cell, direction)) {
                    directions.add(direction);
                }
            }
            double fraction = 1.0 / directions.size();
            double rnd = Math.random();
            _lastDirection = directions.get((int) (rnd / fraction));
        }
        return _lastDirection;
    }
}