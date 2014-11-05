package com.example.pac.pacman;

import java.util.ArrayList;

public class DummyRandomMoveStrategy implements IMoveStrategy {

    private Labyrinth.Cell _lastCell;
    private Direction _lastDirection;

    public DummyRandomMoveStrategy() {}

    @Override
    public Direction getNextDirection(float x, float y) {
        Labyrinth.Cell cell = GameEnv.getInstance().getLabyrinth().cellAt(x, y);
        if (!cell.equals(_lastCell)) {
            _lastCell = cell;
            ArrayList<Direction> directions = new ArrayList<Direction>();
            for (Direction direction : Direction.values()) {
                if (GameEnv.getInstance().getLabyrinth().canMove(cell, direction)) {
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