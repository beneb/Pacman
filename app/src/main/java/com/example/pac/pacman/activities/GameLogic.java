package com.example.pac.pacman.activities;

import android.content.res.Resources;

import com.example.pac.pacman.*;
import com.example.pac.pacman.Character;
import com.example.pac.pacman.event.ChangeLifesEvent;
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

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameLogic {

    private PacMan _pacMan;
    private IEventManager _eventManager;
    private Collection<com.example.pac.pacman.Character> _ghosts;
    private Resources _resources;
    private ArrayList<ActionAfterTimeOut> _actions = new ArrayList<>();
    private Labyrinth _labyrinth;

    public GameLogic(PacMan pacMan, IEventManager eventManager, Collection<Character> ghosts, Labyrinth labyrinth, Resources resources) {
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

            // Remove all similar actions (e.g. another energizer was eaten before)
            ArrayList<ActionAfterTimeOut> actionsToRemove = new ArrayList<ActionAfterTimeOut>();

            for (ActionAfterTimeOut action : _actions) {
                //if (action.TypeOfActionEvent() == EnergizerWillBeRunningOutEvent.class ||
                //    action.TypeOfActionEvent() == EnergizerEndsEvent.class) {

                    actionsToRemove.add(action);
                //}
            }

            _actions.removeAll(actionsToRemove);


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

        } else {
            List<Ghost> interactingGhosts = GetAllGhostsWhoInteractWithPacMan();

            if (!interactingGhosts.isEmpty()) {
                if (_pacMan.IsUnbreakable()) {

                    for (Ghost ghost : interactingGhosts) {
                        _pacMan.EatGhost();
                        int score = (int) Math.pow(2, (double)_pacMan.GetEatenGhostsInARow()) * 100;
                        score = score > 1600 ? 1600 : score;
                        _eventManager.fire(new GhostEatenEvent(score));
                        ghost.wasEaten(score);
                    }
                } else {
                    _eventManager.fire(new ChangeLifesEvent(false)); // reduce lifes
                }
            }
        }
    }

    private List<Ghost> GetAllGhostsWhoInteractWithPacMan() {
        List<Ghost> ghosts = new ArrayList<>();
        int pacMansCell = _labyrinth.getCharacterPosition(_pacMan);

        for (Character character : _ghosts) {
            Ghost ghost = (Ghost) character;

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

    private void HandleAllPendingActions() {
        if (_actions.isEmpty()) {
            return;
        }

        ArrayList<ActionAfterTimeOut> actionsToRemove = new ArrayList<>();

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

