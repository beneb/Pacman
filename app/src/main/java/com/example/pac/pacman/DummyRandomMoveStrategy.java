package com.example.pac.pacman;

import java.util.ArrayList;

public class DummyRandomMoveStrategy implements IMoveStrategy {

    private Labyrinth.Cell _lastCell;
    private Direction _lastDirection;

    public DummyRandomMoveStrategy() {
    }

    @Override
    public Direction getNextDirection(float x, float y) {
        Labyrinth.Cell cell = GameEnv.getInstance().getLabyrinth().cellAt(x, y);
        if (_lastCell == null || (!cell.equals(_lastCell) && !GameEnv.getInstance().getLabyrinth().canMove(cell, _lastDirection))) {
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
        } else {
            _lastCell = cell;
            boolean res = GameEnv.getInstance().getLabyrinth().canMove(_lastCell, _lastDirection);
        }
        return _lastDirection;
    }
}