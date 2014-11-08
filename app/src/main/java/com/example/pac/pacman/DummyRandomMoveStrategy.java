package com.example.pac.pacman;

import java.util.ArrayList;

public class DummyRandomMoveStrategy implements IMoveStrategy {

    private int _lastCell = -1;
    private Direction _lastDirection;

    public DummyRandomMoveStrategy() {
    }

    @Override
    public Direction getNextDirection(float x, float y) {
        int cellNum = GameEnv.getInstance().getLabyrinth().cellAt(x, y);
        if (_lastCell == -1 || (cellNum != _lastCell && !GameEnv.getInstance().getLabyrinth().canMove(cellNum, _lastDirection))) {
            _lastCell = cellNum;
            ArrayList<Direction> directions = new ArrayList<Direction>();
            for (Direction direction : Direction.values()) {
                if (GameEnv.getInstance().getLabyrinth().canMove(cellNum, direction)) {
                    directions.add(direction);
                }
            }
            double fraction = 1.0 / directions.size();
            double rnd = Math.random();
            _lastDirection = directions.get((int) (rnd / fraction));
        } else {
            _lastCell = cellNum;
        }
        return _lastDirection;
    }
}