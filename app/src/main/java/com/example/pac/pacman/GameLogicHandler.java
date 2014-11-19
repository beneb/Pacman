package com.example.pac.pacman;

import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.EventManager;

public class GameLogicHandler {

    private CollisionDetection _collisionDetection;
    private Character _pacMan;
    private EventManager _eventManager;

    public GameLogicHandler(CollisionDetection collisionDetection, PacMan pacMan, EventManager eventManager) {
        _collisionDetection = collisionDetection;
        _pacMan = pacMan;
        _eventManager = eventManager;
    }

    public void HandleAllCollisions() {
        // Handle here all possible collisions in the correct order

        if (_collisionDetection.PacManCanEatADot(_pacMan)) {
            _eventManager.fire(new DotEatenEvent(_pacMan.getCell()));
        }

        // TODO: further collisions like   Ghost <-with-> Pac-Man
        // ...
    }

}

