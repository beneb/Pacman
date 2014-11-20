package com.example.pac.pacman;

import com.example.pac.pacman.event.ChangeHitPointsEvent;
import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.EventManager;
import com.example.pac.pacman.event.InvalidateRectInViewEvent;

import java.util.List;

public class GameLogicHandler {

    private CollisionDetection _collisionDetection;
    private PacMan _pacMan;
    private EventManager _eventManager;
    private List<Character> _characters;

    public GameLogicHandler(CollisionDetection collisionDetection, PacMan pacMan, EventManager eventManager, List<Character> characters) {
        _collisionDetection = collisionDetection;
        _pacMan = pacMan;
        _eventManager = eventManager;
        _characters = characters;
    }

    public void MoveAllCharacters() {
        for (Character character : _characters) {
            character.move();
            _eventManager.fire(new InvalidateRectInViewEvent(character.getInvalidateRect()));
        }
        _pacMan.move();
        _eventManager.fire(new InvalidateRectInViewEvent(_pacMan.getInvalidateRect()));
    }

    public void HandleAllCollisions() {
        // Handle here all possible collisions in the correct order

        if (_collisionDetection.PacManCanEatADot(_pacMan)) {
            _eventManager.fire(new DotEatenEvent(_pacMan.getCell()));
        }

        if (_collisionDetection.PacManInteractWithAGhost(_pacMan, _characters)) {

            // TODO detect if pac-man is immortal due to a eaten pill
            _eventManager.fire(new ChangeHitPointsEvent(false)); // reduce hit points
        }
    }

}

