package com.example.pac.pacman;

import java.util.Date;
import java.util.Random;

/**
 * Created by Viktor on 04.11.2014.
 */

public class DummyRandomMoveStrategy extends MoveStrategy {
    @Override
    public Character.Direction GetNextDirection(Ghost performer) {
        Character.Direction directionToUse = performer._direction;
        FloatPoint newWayPoint = performer.GetNewWayPoint(directionToUse, performer.GetMoveDelta());

        if (performer.canMove(newWayPoint.x, newWayPoint.y)) {
            return directionToUse;
        } else {
            // choose next random direction
            Random rnd = new Random(new Date().getTime());
            int rndInt = rnd.nextInt(4);
            return Character.Direction.values()[rndInt];
        }
    }
}