package com.example.pac.pacman.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pac.pacman.Character;
import com.example.pac.pacman.Direction;
import com.example.pac.pacman.GameEnv;
import com.example.pac.pacman.GhostRepository;
import com.example.pac.pacman.Labyrinth;
import com.example.pac.pacman.PacMan;
import com.example.pac.pacman.PacManMoveStrategy;
import com.example.pac.pacman.R;
import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.EventManager;
import com.example.pac.pacman.event.PacManDirectionRequested;
import com.example.pac.pacman.views.GameplayView;

import java.util.ArrayList;
import java.util.List;

public class PacmanActivity extends ActionBarActivity {
    private int EventDotsScore;
    public EventListener<DotEatenEvent> DotEventListener = new EventListener<DotEatenEvent>() {
        @Override
        public void onEvent(DotEatenEvent event) {
            EventDotsScore++;
        }
    };

    public EventListener<PacManDirectionRequested> _directionChangedListener = new EventListener<PacManDirectionRequested>() {
        @Override
        public void onEvent(PacManDirectionRequested event) {

            PointF curPos = _pacMan.getPosition();
            if (Math.abs(event.getNewX() - curPos.x) > Math.abs(event.getNewY() - curPos.y)) { // horizontal move
                if (event.getNewX() < curPos.x) {
                    _pacManStrategy.setWishDirection(Direction.Left);
                } else {
                    _pacManStrategy.setWishDirection(Direction.Right);
                }
            } else { // vertical move
                if (event.getNewY() < curPos.y) {
                    _pacManStrategy.setWishDirection(Direction.Up);
                } else {
                    _pacManStrategy.setWishDirection(Direction.Down);
                }
            }
        }
    };

    public static final String RESUME_ACTION = "RESUME";
    public static final String SETTINGS = "SETTINGS";
    public static final String LABYRINTH_STATE = "LABYRINTH_STATE";

    private Handler _handler = new Handler();
    private GameplayView _view;
    private List<com.example.pac.pacman.Character> _characters;
    private Labyrinth _labyrinth;
    private int tmp = 0;
    private EventManager _eventManager = new EventManager();
    private PacMan _pacMan;
    private PacManMoveStrategy _pacManStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String state = loadLabyrinthState(action);

        _labyrinth = new Labyrinth(state, getResources());
        GameEnv.getInstance().InitOnce(getResources());

        _characters = new ArrayList<Character>();
        _pacManStrategy = new PacManMoveStrategy(_labyrinth);
        _pacMan = new PacMan(getResources().getColor(R.color.pacman), _pacManStrategy, _labyrinth);
        _characters.addAll(GhostRepository.CreateGhosts(getResources(), _labyrinth));

        _eventManager.registerObserver(DotEatenEvent.class, DotEventListener);
        _eventManager.registerObserver(PacManDirectionRequested.class, _directionChangedListener);

        _view = new GameplayView(this, _eventManager, _labyrinth, _pacMan, _characters);
        // setContentView(_view);

        setContentView(R.layout.activity_pacman);
        final RelativeLayout frame = (RelativeLayout) findViewById(R.id.frame);
        frame.addView(_view);

        _handler.postDelayed(_updateView, 1000);
    }

    private String loadLabyrinthState(String action) {
        String state = getResources().getString(R.string.level_classic);
        if (RESUME_ACTION.equals(action)) {
            SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
            state = settings.getString(LABYRINTH_STATE, state);
        }
        return state;
    }

    private Runnable _updateView = new Runnable() {
        public void run() {
            moveAll();
            eatDots();

            _handler.removeCallbacks(_updateView);
            _handler.postDelayed(this, 30);
            final TextView score = (TextView) findViewById(R.id.scoreTextView);
            score.setText("Score:" + EventDotsScore);
        }
    };

    private void moveAll() {
        for (Character character : _characters) {
            character.move();
            _view.invalidate(character.getInvalidateRect());
        }
        _pacMan.move();
        _view.invalidate(_pacMan.getInvalidateRect());
    }

    private void eatDots() {
        if (_labyrinth.eatDot(_pacMan)) {
            _eventManager.fire(new DotEatenEvent());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveLabyrinthState();

        _handler.removeCallbacks(_updateView);
        _eventManager.unregisterAll();
    }

    private void saveLabyrinthState() {
        SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(LABYRINTH_STATE, _labyrinth.getState());
        editor.apply();
    }
}




