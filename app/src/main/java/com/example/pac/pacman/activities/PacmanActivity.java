package com.example.pac.pacman.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.example.pac.pacman.Character;
import com.example.pac.pacman.CollisionDetection;
import com.example.pac.pacman.GameEnv;
import com.example.pac.pacman.GameLogicHandler;
import com.example.pac.pacman.GhostRepository;
import com.example.pac.pacman.IMoveStrategy;
import com.example.pac.pacman.InputHandler;
import com.example.pac.pacman.Labyrinth;
import com.example.pac.pacman.PacMan;
import com.example.pac.pacman.PacManMoveStrategy;
import com.example.pac.pacman.R;
import com.example.pac.pacman.SoundHandler;
import com.example.pac.pacman.event.ChangeHitPointsEvent;
import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.DrawRequestEvent;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.EventManager;
import com.example.pac.pacman.event.InitEvent;
import com.example.pac.pacman.event.InvalidateViewEvent;
import com.example.pac.pacman.util.Fonts;
import com.example.pac.pacman.views.GameplayView;

import java.util.ArrayList;
import java.util.List;

public class PacmanActivity extends ActionBarActivity {
    private int EventDotsScore;
    private boolean _pacManWasHit;

    public EventListener<DotEatenEvent> DotEventListener = new EventListener<DotEatenEvent>() {
        @Override
        public void onEvent(DotEatenEvent event) {
            EventDotsScore++;
        }
    };

    public EventListener<ChangeHitPointsEvent> ChangeHitPoints = new EventListener<ChangeHitPointsEvent>() {
        @Override
        public void onEvent(ChangeHitPointsEvent event) {
            _pacManWasHit = !event.IncreaseHitPoints();
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
    private InputHandler _inputHandler;
    private SoundHandler _soundHandler;
    private GameLogicHandler _gameLogicHandler;

    private TextView _score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String state = loadLabyrinthState(action);

        _labyrinth = new Labyrinth(state, getResources());
        GameEnv.getInstance().InitOnce(getResources());


        IMoveStrategy pacManStrategy = new PacManMoveStrategy(_labyrinth);
        _pacMan = new PacMan(getResources().getColor(R.color.pacman), pacManStrategy, _labyrinth);
        _inputHandler = new InputHandler(_pacMan, pacManStrategy, _eventManager);
        _soundHandler = new SoundHandler(this, _eventManager);

        _characters = new ArrayList<Character>();
        _characters.addAll(GhostRepository.CreateGhosts(getResources(), _labyrinth));

        _gameLogicHandler = new GameLogicHandler(new CollisionDetection(_labyrinth), _pacMan, _eventManager, _characters, _labyrinth);

        setContentView(R.layout.activity_pacman);
        _view = (GameplayView) findViewById(R.id.gameplay_view);
        _view.init(_eventManager);

        Fonts.setRegularFont(this, R.id.score_label);
        Fonts.setRegularFontWithColor(this, R.id.textScore, Color.YELLOW);
        Fonts.setRegularFont(this, R.id.ouchTextView);

        _eventManager.registerObserver(InitEvent.class, _gameLogicHandler.InitGameListener);
        _eventManager.registerObserver(DrawRequestEvent.class, _gameLogicHandler.DrawRequestListener);
        _eventManager.registerObserver(DotEatenEvent.class, _labyrinth.DotEventListener);
        _eventManager.registerObserver(DotEatenEvent.class, DotEventListener);
        _eventManager.registerObserver(ChangeHitPointsEvent.class, ChangeHitPoints);

        _eventManager.registerObserver(InvalidateViewEvent.class, _view.InvalidateListener);

        // Score view
        _score = (TextView) findViewById(R.id.textScore);

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
            _gameLogicHandler.MoveAllCharacters();
            _gameLogicHandler.HandleAllCollisions();

            _score.setText("" + EventDotsScore);
            final TextView ouchTxt = (TextView) findViewById(R.id.ouchTextView);
            ouchTxt.setTextColor(Color.RED);
            ouchTxt.setText(_pacManWasHit ? "OUCH!!!" : "");

            _handler.removeCallbacks(_updateView);
            _handler.postDelayed(this, 30);
        }
    };


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




