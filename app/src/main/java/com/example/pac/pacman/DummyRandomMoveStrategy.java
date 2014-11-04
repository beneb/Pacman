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

        if (directionToUse == Character.Direction.Stopped) {
            return GetRandomDirectionWithoutStopped();
        }

        FloatPoint newWayPoint = performer.GetNewWayPoint(directionToUse, performer.GetMoveDelta());

        while (!performer.canMove(newWayPoint.x, newWayPoint.y)) {
            directionToUse = GetRandomDirectionWithoutStopped();
        }

        return directionToUse;
    }

    private Character.Direction GetRandomDirectionWithoutStopped() {
        // choose next random direction
        Character.Direction newDirection;

        do {
            Random rnd = new Random(new Date().getTime());
            int rndInt = rnd.nextInt(4);
            newDirection = Character.Direction.values()[rndInt];
        } while (newDirection == Character.Direction.Stopped);

        return newDirection;
    }
}