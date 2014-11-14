package com.example.pac.pacman;

import java.util.ArrayList;

public class DummyRandomMoveStrategy implements IMoveStrategy {

    private int _lastCell = -1;
    private Direction _lastDirection;
    private Labyrinth _labyrinth;

    public DummyRandomMoveStrategy(Labyrinth labyrinth) {
        _labyrinth = labyrinth;
    }

    @Override
    public Direction GetCurrentOrNextDirection(float currentX, float currentY) {
        int currentCell = _labyrinth.cellAt(currentX, currentY);
        if (_lastCell == -1 ||
            (
             !_labyrinth.canMoveWithinCurrentCell(currentX, currentY, currentCell, _lastDirection) &&
             !_labyrinth.canMove(currentCell, _lastDirection))) {

            _lastCell = currentCell;
            ArrayList<Direction> directions = new ArrayList<Direction>();
            for (Direction direction : Direction.values()) {
                if (_labyrinth.canMove(currentCell, direction)) {
                    directions.add(direction);
                }
            }
            if (directions.isEmpty()) {
                directions.add(Direction.Right);
            }
            double fraction = 1.0 / directions.size();
            double rnd = Math.random();
            _lastDirection = directions.get((int) (rnd / fraction));
        } else {
            _lastCell = currentCell;
        }
        return _lastDirection;
    }
}
