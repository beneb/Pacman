package com.example.pac.pacman.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.example.pac.pacman.Character;
import com.example.pac.pacman.GameLogicHandler;
import com.example.pac.pacman.Ghost;
import com.example.pac.pacman.GhostRepository;
import com.example.pac.pacman.IMoveStrategy;
import com.example.pac.pacman.InputHandler;
import com.example.pac.pacman.Labyrinth;
import com.example.pac.pacman.PacMan;
import com.example.pac.pacman.PacManMoveStrategy;
import com.example.pac.pacman.R;
import com.example.pac.pacman.SoundHandler;
import com.example.pac.pacman.event.EnergizerEatenEvent;
import com.example.pac.pacman.event.ChangeLifesEvent;
import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.DrawRequestEvent;
import com.example.pac.pacman.event.EnergizerEndsEvent;
import com.example.pac.pacman.event.EnergizerWillBeRunningOutEvent;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.EventManager;
import com.example.pac.pacman.event.IEventManager;
import com.example.pac.pacman.event.InitEvent;
import com.example.pac.pacman.event.LevelCompleteEvent;
import com.example.pac.pacman.event.PacManDirectionRequestEvent;
import com.example.pac.pacman.util.Fonts;
import com.example.pac.pacman.views.GameplayView;
import com.example.pac.pacman.views.IChildView;
import com.example.pac.pacman.views.LabyrinthView;
import com.example.pac.pacman.views.PacManView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PacmanActivity extends ActionBarActivity {
    private int _score;

    public EventListener<EnergizerEatenEvent> EnergizerStartsListener =
            new EventListener<EnergizerEatenEvent>() {
                @Override
                public void onEvent(EnergizerEatenEvent event) {
                    setInfoLabel("Superman!", Color.BLUE);
                }
            };

    public EventListener<EnergizerWillBeRunningOutEvent> EnergizerWillBeRunningOutListener =
            new EventListener<EnergizerWillBeRunningOutEvent>() {
        @Override
        public void onEvent(EnergizerWillBeRunningOutEvent event) {
            setInfoLabel("Hurry up!", Color.YELLOW);
        }
    };

    public EventListener<EnergizerEndsEvent> EnergizerEndsListener =
            new EventListener<EnergizerEndsEvent>() {
                @Override
                public void onEvent(EnergizerEndsEvent event) {
                    setInfoLabel("", Color.BLACK);
                }
            };

    public EventListener<DotEatenEvent> DotEatenListener = new EventListener<DotEatenEvent>() {
        @Override
        public void onEvent(DotEatenEvent event) {
            setScore(_score += 10);
        }
    };

    public EventListener<EnergizerEatenEvent> EnergizerEatenListener = new EventListener<EnergizerEatenEvent>() {
        @Override
        public void onEvent(EnergizerEatenEvent event) {
            setScore(_score += 50);
        }
    };

    public EventListener<ChangeLifesEvent> ChangeHitPoints = new EventListener<ChangeLifesEvent>() {
        @Override
        public void onEvent(ChangeLifesEvent event) {
            boolean pacManWasHit = !event.OneUp();
            setInfoLabel(pacManWasHit ? "OUCH!!!" : "", Color.RED);
        }
    };

    private void setScore(int score) {
        TextView v = (TextView) findViewById(R.id.score_value);
        v.setText("" + score);
    }

    private void setInfoLabel(String text, int color) {
        final TextView ouchTxt = (TextView) findViewById(R.id.ouchTextView);
        ouchTxt.setTextColor(color);
        ouchTxt.setText(text);
    }

    private IEventManager _eventManager = new EventManager();
    private FrameLoop _frameLoop;

    private Labyrinth _labyrinth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pacman);

        // Fonts.setRegularFont(this, R.id.score_label);
        Fonts.setRegularFont(this, R.id.high_score_label);
        Fonts.setRegularFont(this, R.id.high_score_value);
        Fonts.setRegularFont(this, R.id.score_value);
        Fonts.setRegularFont(this, R.id.ouchTextView);

        initState();

        GameLogicHandler gameLogic = createGameObjects();

        _frameLoop = new FrameLoop(gameLogic);
        _frameLoop.start();
    }

    private void initState() {
        _state.load();
        _score = _state._score;
    }

    @Override
    protected void onStop() {
        super.onStop();
        _state.save(_labyrinth);

        _frameLoop.stop();
        _eventManager.unregisterAll();
    }

    private GameLogicHandler createGameObjects() {
        _labyrinth = new Labyrinth(_state.getLabyrinthState());

        setScore(_state.getScore());

        IMoveStrategy pacManStrategy = new PacManMoveStrategy(_labyrinth);
        PacMan pacMan = new PacMan(pacManStrategy, _labyrinth);
        PacManView pacManView = new PacManView(pacMan, getResources());

        Map<Character, IChildView> ghosts = GhostRepository.CreateGhosts(getResources(), _labyrinth);

        GameLogicHandler gameLogic = new GameLogicHandler(pacMan,_eventManager, ghosts.keySet(), _labyrinth, getResources());
        SoundHandler soundHandler = new SoundHandler(this);

        _eventManager.registerObserver(InitEvent.class, gameLogic.InitGameListener);
        _eventManager.registerObserver(DotEatenEvent.class, DotEatenListener);
        _eventManager.registerObserver(EnergizerEatenEvent.class, EnergizerEatenListener);

        _eventManager.registerObserver(ChangeLifesEvent.class, ChangeHitPoints);
        _eventManager.registerObserver(PacManDirectionRequestEvent.class, new InputHandler(pacMan, pacManStrategy).DirectionChangedListener);
        _eventManager.registerObserver(DotEatenEvent.class, soundHandler.PlaySoundForEatingADot);
        _eventManager.registerObserver(EnergizerEatenEvent.class, soundHandler.PlaySoundForEatingAnEnergizer);
        _eventManager.registerObserver(EnergizerEatenEvent.class, EnergizerStartsListener);
        _eventManager.registerObserver(EnergizerWillBeRunningOutEvent.class, EnergizerWillBeRunningOutListener);
        _eventManager.registerObserver(EnergizerEndsEvent.class, EnergizerEndsListener);
        _eventManager.registerObserver(LevelCompleteEvent.class, LevelCompleteHandler);

        _eventManager.registerObserver(EnergizerEatenEvent.class, pacMan.EnergizerStartsListener);
        _eventManager.registerObserver(EnergizerEndsEvent.class, pacMan.EnergizerEndsListener);

        for (Character character : ghosts.keySet()) {
            _eventManager.registerObserver(EnergizerEndsEvent.class, ((Ghost) character).EnergizerEndsListener);
            _eventManager.registerObserver(EnergizerEatenEvent.class, ((Ghost) character).EnergizerStartsListener);
            _eventManager.registerObserver(EnergizerWillBeRunningOutEvent.class, ((Ghost)character).EnergizerWillBeRunningOutListener);
        }

        GameplayView gameplayView = (GameplayView) findViewById(R.id.gameplay_view);

        Collection<IChildView> childViews = new ArrayList<IChildView>(ghosts.values());
        childViews.add(pacManView);
        childViews.add(new LabyrinthView(_labyrinth, getResources()));
        gameplayView.init(_eventManager, childViews);

        return gameLogic;
    }


    public EventListener<LevelCompleteEvent> LevelCompleteHandler = new EventListener<LevelCompleteEvent>() {
        @Override
        public void onEvent(LevelCompleteEvent event) {
            _frameLoop.stop();
            setInfoLabel("Level Complete!", Color.GREEN);
            Handler levelCompleteDelayHandler = new Handler();
            levelCompleteDelayHandler.postDelayed(new Runnable() {
                public void run() {
                    setInfoLabel("", Color.RED);
                    _labyrinth.load(_state.getNewLevel());
                    _eventManager.fire(new InitEvent());
                    _frameLoop.start();
                }
            }, 3000);
        }
    };

    State _state = new State();

    class State {
        public static final String RESUME_ACTION = "RESUME";
        private static final String SETTINGS = "SETTINGS";
        private static final String LABYRINTH_STATE = "LABYRINTH_STATE";
        private static final String SCORE = "SCORE";

        private String _labyrinthState;
        private int _score;

        public String getLabyrinthState() {
            return _labyrinthState;
        }

        public String getNewLevel(){
            return getResources().getString(R.string.level_classic);
        }

        public int getScore() {
            return _score;
        }

        private void load() {
            Intent intent = getIntent();
            String action = intent.getAction();
            _labyrinthState = getNewLevel();
            if (RESUME_ACTION.equals(action)) {
                SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
                _labyrinthState = settings.getString(LABYRINTH_STATE, _labyrinthState);
                _score = settings.getInt(SCORE, _score);
            }
        }

        private void save(Labyrinth labyrinth) {
            SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
            SharedPreferences.Editor editor = settings.edit();
            _labyrinthState = labyrinth.getState();
            editor.putString(LABYRINTH_STATE, _labyrinthState);
            TextView view =  (TextView) findViewById(R.id.score_value);
            _score = Integer.parseInt(view.getText().toString());
            editor.putInt(SCORE, _score);
            editor.apply();
        }
    }
}




