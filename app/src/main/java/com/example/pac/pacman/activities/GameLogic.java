package com.example.pac.pacman.activities;

import com.example.pac.pacman.Character;
import com.example.pac.pacman.Ghost;
import com.example.pac.pacman.GhostMode;
import com.example.pac.pacman.Labyrinth;
import com.example.pac.pacman.PacMan;
import com.example.pac.pacman.StopMoveStrategy;
import com.example.pac.pacman.event.PacManDeadEvent;
import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.EnergizerEatenEvent;
import com.example.pac.pacman.event.EnergizerEndsEvent;
import com.example.pac.pacman.event.EnergizerWillBeRunningOutEvent;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.GhostEatenEvent;
import com.example.pac.pacman.event.IEventManager;
import com.example.pac.pacman.event.InitEvent;
import com.example.pac.pacman.event.InvalidateViewEvent;
import com.example.pac.pacman.event.LevelCompleteEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameLogic {

    private PacMan _pacMan;
    private IEventManager _eventManager;
    private Collection<Ghost> _ghosts;
    private Labyrinth _labyrinth;

    public final int ENERGIZER_RUNNING_OUT_DURATION = 7000;
    public final int ENERGIZER_DURATION = 10000;

    public GameLogic(PacMan pacMan, IEventManager eventManager, Collection<Ghost> ghosts, Labyrinth labyrinth) {
        _labyrinth = labyrinth;
        _pacMan = pacMan;
        _eventManager = eventManager;
        _ghosts = ghosts;
    }

    public void UpdateOnFrame() {
        for (Character g : _ghosts) {
            g.move();

            _eventManager.fire(new InvalidateViewEvent(g.getInvalidateRect()));
        }
        _pacMan.move();
        _eventManager.fire(new InvalidateViewEvent(_pacMan.getInvalidateRect()));

        HandleAllCollisions();
    }

    private void HandleAllCollisions() {
        // Handle here all possible collisions in the correct order

        if (_labyrinth.tryEatDot(_pacMan)) {
            _eventManager.fire(new DotEatenEvent(_pacMan.getCell()));
        }
        if (_labyrinth.tryEatEnergizer(_pacMan)) {
            _eventManager.fire(new EnergizerEatenEvent());
            _eventManager.fire(new EnergizerWillBeRunningOutEvent(), ENERGIZER_RUNNING_OUT_DURATION);
            _eventManager.fire(new EnergizerEndsEvent(), ENERGIZER_DURATION);
        }

        if (!_labyrinth.hasDots()) {
            _eventManager.fire(new LevelCompleteEvent());
        } else {
            List<Ghost> interactingGhosts = GetAllGhostsWhoInteractWithPacMan();

            if (!interactingGhosts.isEmpty()) {
                if (_pacMan.isUnbreakable()) {
                    for (Ghost g : interactingGhosts) {

                        if (g.TryToEatThisGhost()) {
                            int score = _pacMan.EatGhost(g);
                            _eventManager.fire(new GhostEatenEvent(g, score));
                        }
                    }
                } else if (!_pacMan.isDead()){
                    for (Ghost g : _ghosts) {
                        g.Hide();
                    }
                    _pacMan.setMoveStrategy(new StopMoveStrategy());
                    _pacMan.setDead();
                    _eventManager.fire(new PacManDeadEvent());
                }
            }
        }
    }

    private List<Ghost> GetAllGhostsWhoInteractWithPacMan() {
        List<Ghost> ghosts = new ArrayList<>();
        int pacMansCell = _labyrinth.getCharacterPosition(_pacMan);

        for (Ghost ghost : _ghosts) {
            if (ghost.getMode() == GhostMode.FadeAwayAndShowingScore ||
                ghost.getMode() == GhostMode.WalkingBack) {
                continue;
            }

            int ghostsCell = _labyrinth.getCharacterPosition(ghost);
            if (ghostsCell == pacMansCell) {
                ghosts.add(ghost);
            }
        }

        return ghosts;
    }

    public EventListener<InitEvent> InitGameListener = new EventListener<InitEvent>() {
        @Override
        public void onEvent(InitEvent event) {
            if (event.getBounds() != null) {
                _labyrinth.init(event.getBounds());
            }
            for (Character g : _ghosts) {
                g.init();
            }
            _pacMan.init();
        }
    };

    public EventListener<EnergizerEatenEvent> EnergizerStartsListener =
            new EventListener<EnergizerEatenEvent>() {
                @Override
                public void onEvent(EnergizerEatenEvent event) {
                    _pacMan.setUnbreakable(true);
                    for (Ghost g : _ghosts) {
                        g.TryToScare();
                    }
                }
            };

    public EventListener<EnergizerWillBeRunningOutEvent> EnergizerWillBeRunningOutListener =
            new EventListener<EnergizerWillBeRunningOutEvent>() {
                @Override
                public void onEvent(EnergizerWillBeRunningOutEvent event) {
                    for (Ghost g : _ghosts) {
                        g.TryToFlash();
                    }
                }
            };

    public EventListener<EnergizerEndsEvent> EnergizerEndsListener =
            new EventListener<EnergizerEndsEvent>() {
                @Override
                public void onEvent(EnergizerEndsEvent event) {
                    _pacMan.setUnbreakable(false);
                    for (Ghost g : _ghosts) {
                        g.TryToCalmDown();

                    }
                }
            };

}

