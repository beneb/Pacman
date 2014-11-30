package com.example.pac.pacman;

import android.content.res.Resources;

import com.example.pac.pacman.event.ChangeLifesEvent;
import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.EnergizerEatenEvent;
import com.example.pac.pacman.event.EnergizerEndsEvent;
import com.example.pac.pacman.event.EnergizerWillBeRunningOutEvent;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.IEventManager;
import com.example.pac.pacman.event.InitEvent;
import com.example.pac.pacman.event.InvalidateViewEvent;
import com.example.pac.pacman.event.LevelCompleteEvent;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;

public class GameLogicHandler {

    private PacMan _pacMan;
    private IEventManager _eventManager;
    private Collection<Character> _ghosts;
    private Resources _resources;
    private ArrayList<ActionAfterTimeOut> _actions = new ArrayList<ActionAfterTimeOut>();
    private Labyrinth _labyrinth;

    public GameLogicHandler(PacMan pacMan, IEventManager eventManager, Collection<Character> ghosts, Labyrinth labyrinth, Resources resources) {
        _labyrinth = labyrinth;
        _pacMan = pacMan;
        _eventManager = eventManager;
        _ghosts = ghosts;
        _resources = resources;
    }

    public void UpdateOnFrame() {
        for (Character character : _ghosts) {
            character.move();
            _eventManager.fire(new InvalidateViewEvent(character.getInvalidateRect()));
        }
        _pacMan.move();
        _eventManager.fire(new InvalidateViewEvent(_pacMan.getInvalidateRect()));

        HandleAllCollisions();
        HandleAllPendingActions();
    }

    private void HandleAllCollisions() {
        // Handle here all possible collisions in the correct order

        if (_labyrinth.eatDot(_pacMan)) {
            _eventManager.fire(new DotEatenEvent(_pacMan.getCell()));
        }
        if (_labyrinth.eatEnergizer(_pacMan)) {
            _eventManager.fire(new EnergizerEatenEvent(_pacMan.getCell()));

            // TODO: remove all similar actions (another energizer was eaten before)

            int durationUntilEnergizerWillBeRunningOutEvent = _resources.getInteger(R.integer.DurationOfEnergizer) -
                    _resources.getInteger(R.integer.DurationBeforeEnergizerTimedOut);
            _actions.add(new ActionAfterTimeOut(DateTime.now().plusSeconds(
                    durationUntilEnergizerWillBeRunningOutEvent),
                    new EnergizerWillBeRunningOutEvent(), _eventManager));

            _actions.add(new ActionAfterTimeOut(DateTime.now().plusSeconds(
                    _resources.getInteger(R.integer.DurationOfEnergizer)),
                    new EnergizerEndsEvent(), _eventManager));
        }

        if (!_labyrinth.haveDots()) {
            _eventManager.fire(new LevelCompleteEvent());
        } else if (PacManInteractWithAGhost()) {

            if (!_pacMan.IsUnbreakable()) {
                _eventManager.fire(new ChangeLifesEvent(false)); // reduce lifes
            }
        }
    }

    public boolean PacManInteractWithAGhost() {
        int pacMansCell = _labyrinth.getCharacterPosition(_pacMan);

        for (Character ghost : _ghosts) {
            int ghostsCell = _labyrinth.getCharacterPosition(ghost);
            if (ghostsCell == pacMansCell) {
                return true;
            }
        }

        return false;
    }

    private void HandleAllPendingActions() {
        if (_actions.isEmpty()) {
            return;
        }

        ArrayList<ActionAfterTimeOut> actionsToRemove = new ArrayList<ActionAfterTimeOut>();

        for (ActionAfterTimeOut action : _actions) {
            if (action.FireAndForget()) {
                actionsToRemove.add(action);
            }
        }

        _actions.removeAll(actionsToRemove);
    }

    public EventListener<InitEvent> InitGameListener = new EventListener<InitEvent>() {
        @Override
        public void onEvent(InitEvent event) {
            if (event.getBounds() != null) {
                _labyrinth.init(event.getBounds());
            }
            for (Character ch : _ghosts) {
                ch.init();
            }
            _pacMan.init();
        }
    };
}

