package com.example.pac.pacman.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.example.pac.pacman.Character;
import com.example.pac.pacman.CollisionDetection;
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

public class PacmanActivity extends ActionBarActivity {
    private int EventDotsScore;
    private boolean _pacManWasHit;

    public EventListener<DotEatenEvent> DotEatenListener = new EventListener<DotEatenEvent>() {
        @Override
        public void onEvent(DotEatenEvent event) {
            EventDotsScore++;
            _score.setText("" + EventDotsScore);
        }
    };

    public EventListener<ChangeHitPointsEvent> ChangeHitPoints = new EventListener<ChangeHitPointsEvent>() {
        @Override
        public void onEvent(ChangeHitPointsEvent event) {
            _pacManWasHit = !event.IncreaseHitPoints();
            final TextView ouchTxt = (TextView) findViewById(R.id.ouchTextView);
            ouchTxt.setTextColor(Color.RED);
            ouchTxt.setText(_pacManWasHit ? "OUCH!!!" : "");
        }
    };


    private Labyrinth _labyrinth;
    private EventManager _eventManager = new EventManager();
    private InputHandler _inputHandler;
    private SoundHandler _soundHandler;

    private TextView _score;

    private FrameLoop _frameLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _state.load();

        _labyrinth = new Labyrinth(_state.getLabyrinthState(), getResources());

        IMoveStrategy pacManStrategy = new PacManMoveStrategy(_labyrinth);
        PacMan pacMan = new PacMan(getResources().getColor(R.color.pacman), pacManStrategy, _labyrinth);
        _inputHandler = new InputHandler(pacMan, pacManStrategy, _eventManager);
        _soundHandler = new SoundHandler(this, _eventManager);

        ArrayList<Character> _characters = new ArrayList<Character>();
        _characters.addAll(GhostRepository.CreateGhosts(getResources(), _labyrinth));

        GameLogicHandler gameLogic = new GameLogicHandler(new CollisionDetection(_labyrinth), pacMan, _eventManager, _characters, _labyrinth);

        setContentView(R.layout.activity_pacman);
        GameplayView gameplayView = (GameplayView) findViewById(R.id.gameplay_view);
        gameplayView.init(_eventManager);

        Fonts.setRegularFont(this, R.id.score_label);
        Fonts.setRegularFont(this, R.id.score_text);
        Fonts.setRegularFont(this, R.id.ouchTextView);

        _eventManager.registerObserver(InitEvent.class, gameLogic.InitGameListener);
        _eventManager.registerObserver(DrawRequestEvent.class, gameLogic.DrawRequestListener);
        _eventManager.registerObserver(DotEatenEvent.class, _labyrinth.DotEventListener);
        _eventManager.registerObserver(DotEatenEvent.class, DotEatenListener);
        _eventManager.registerObserver(ChangeHitPointsEvent.class, ChangeHitPoints);

        _eventManager.registerObserver(InvalidateViewEvent.class, gameplayView.InvalidateListener);

        // Score view
        _score = (TextView) findViewById(R.id.score_text);

        _frameLoop = new FrameLoop(gameLogic);
        _frameLoop.Start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        _state.save(_labyrinth);

        _frameLoop.Destroy();
        _eventManager.unregisterAll();
    }


    State _state = new State();

    class State {
        public static final String RESUME_ACTION = "RESUME";

        private static final String SETTINGS = "SETTINGS";
        private static final String LABYRINTH_STATE = "LABYRINTH_STATE";

        private String _labyrinthState;

        public String getLabyrinthState() {
            return _labyrinthState;
        }

        private void load() {
            Intent intent = getIntent();
            String action = intent.getAction();
            _labyrinthState = getResources().getString(R.string.level_classic);
            if (RESUME_ACTION.equals(action)) {
                SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
                _labyrinthState = settings.getString(LABYRINTH_STATE, _labyrinthState);
                // TODO load score
            }
        }

        private void save(Labyrinth labyrinth) {
            SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
            SharedPreferences.Editor editor = settings.edit();
            _labyrinthState = labyrinth.getState();
            editor.putString(LABYRINTH_STATE, _labyrinthState);
            editor.apply();
        }
    }
}




