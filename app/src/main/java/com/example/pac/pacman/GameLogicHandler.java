package com.example.pac.pacman;

import com.example.pac.pacman.event.BigDotEatenEvent;
import com.example.pac.pacman.event.ChangeHitPointsEvent;
import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.DrawRequestEvent;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.IEventManager;
import com.example.pac.pacman.event.InitEvent;
import com.example.pac.pacman.event.InvalidateViewEvent;

import java.util.List;

public class GameLogicHandler {

    private CollisionDetection _collisionDetection;
    private PacMan _pacMan;
    private IEventManager _eventManager;
    private List<Character> _characters;
    private Labyrinth _labyrinth;

    public GameLogicHandler(CollisionDetection collisionDetection, PacMan pacMan, IEventManager eventManager, List<Character> characters, Labyrinth labyrinth) {
        _collisionDetection = collisionDetection;
        _labyrinth = labyrinth;
        _pacMan = pacMan;
        _eventManager = eventManager;
        _characters = characters;
    }

    public void UpdateOnFrame() {
        for (Character character : _characters) {
            character.move();
            _eventManager.fire(new InvalidateViewEvent(character.getInvalidateRect()));
        }
        _pacMan.move();
        _eventManager.fire(new InvalidateViewEvent(_pacMan.getInvalidateRect()));

        HandleAllCollisions();
    }

    private void HandleAllCollisions() {
        // Handle here all possible collisions in the correct order

        if (_labyrinth.eatDot(_pacMan)) {
            _eventManager.fire(new DotEatenEvent(_pacMan.getCell()));
        }
        if (_labyrinth.eatBigDot(_pacMan)) {
            _eventManager.fire(new BigDotEatenEvent(_pacMan.getCell()));
        }

        if (_collisionDetection.PacManInteractWithAGhost(_pacMan, _characters)) {

            // TODO: detect if pac-man is immortal due to a eaten energizer
            _eventManager.fire(new ChangeHitPointsEvent(false)); // reduce hit points
        }
    }

    public EventListener<DrawRequestEvent> DrawRequestListener = new EventListener<DrawRequestEvent>() {
        @Override
        public void onEvent(DrawRequestEvent event) {
            _labyrinth.getPresentation().draw(event.getCanvas());
            for (Character ch : _characters) {
                ch.draw(event.getCanvas());
            }
            _pacMan.draw(event.getCanvas());
        }
    };

    public EventListener<InitEvent> InitGameListener = new EventListener<InitEvent>() {
        @Override
        public void onEvent(InitEvent event) {
            _labyrinth.init(event.getBounds());
            for (Character ch : _characters) {
                ch.init();
            }
            _pacMan.init();
        }
    };
}

