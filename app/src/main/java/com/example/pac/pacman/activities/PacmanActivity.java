package com.example.pac.pacman.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.example.pac.pacman.Ghost;
import com.example.pac.pacman.GhostRepository;
import com.example.pac.pacman.IMoveStrategy;
import com.example.pac.pacman.Labyrinth;
import com.example.pac.pacman.PacMan;
import com.example.pac.pacman.PacManMoveStrategy;
import com.example.pac.pacman.R;
import com.example.pac.pacman.RandomMoveStrategy;
import com.example.pac.pacman.event.PacManDeadEvent;
import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.EnergizerEatenEvent;
import com.example.pac.pacman.event.EnergizerEndsEvent;
import com.example.pac.pacman.event.EnergizerWillBeRunningOutEvent;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.EventManager;
import com.example.pac.pacman.event.GhostEatenEvent;
import com.example.pac.pacman.event.IEventManager;
import com.example.pac.pacman.event.InitEvent;
import com.example.pac.pacman.event.LevelCompleteEvent;
import com.example.pac.pacman.event.PacManDirectionRequestEvent;
import com.example.pac.pacman.util.Fonts;
import com.example.pac.pacman.views.GameplayView;
import com.example.pac.pacman.views.IChildView;
import com.example.pac.pacman.views.LabyrinthView;
import com.example.pac.pacman.views.NextLevelFragment;
import com.example.pac.pacman.views.PacManView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class PacmanActivity extends ActionBarActivity {
    private int _score;
    private Set<Ghost> _ghosts;
    private PacMan _pacMan;

    public void saveHighScore(View v) {
        Intent intent = new Intent(this, HighScoresActivity.class);
        intent.putExtra("high_score_value", _score);
        startActivity(intent);
    }

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

    public EventListener<GhostEatenEvent> GhostEatenEventListener = new GhostEatenEventListener<GhostEatenEvent>(this) {
        @Override
        public void onEvent(final GhostEatenEvent event) {
            setInfoLabel("Yeah!", Color.RED);
            _score += event.GetScore();
            setScoreView();
            // _pacMan.setHidden(true);
            // TODO: Stop PacMan
            // TODO: Stop all other ghosts

            walkBackDelayed(new Runnable() {
                @Override
                public void run() {
                    event.GetGhostWasEaten().TryToWalkBack();
                    // _pacMan.setHidden(false);
                    // TODO: Let PacMan move again
                    // TODO: Let all other ghosts move again
                }
            });
        }
    };

    public EventListener<DotEatenEvent> DotEatenListener = new EventListener<DotEatenEvent>() {
        @Override
        public void onEvent(DotEatenEvent event) {
            _score += 10;
            setScoreView();
        }
    };

    public EventListener<EnergizerEatenEvent> EnergizerEatenListener = new EventListener<EnergizerEatenEvent>() {
        @Override
        public void onEvent(EnergizerEatenEvent event) {
            _score += 50;
            setScoreView();
        }
    };

    private Collection<IChildView> _childViews;

    private void setScoreView() {
        TextView v = (TextView) findViewById(R.id.score_value);
        v.setText("" + _score);
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

        initState();
        GameLogic gameLogic = createGameObjects();
        initViews();

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_pacman);
        initViews();
    }

    private GameLogic createGameObjects() {
        _labyrinth = new Labyrinth(_state.getLabyrinthState());

        _score = _state.getScore();
        setScoreView();

        IMoveStrategy pacManStrategy = new PacManMoveStrategy(_labyrinth);
        _pacMan = new PacMan(pacManStrategy, _labyrinth);
        PacManView pacManView = new PacManView(_pacMan, getResources());

        Map<Ghost, IChildView> ghosts = GhostRepository.CreateGhosts(getResources(), _labyrinth);

        _ghosts = ghosts.keySet();
        GameLogic gameLogic = new GameLogic(_pacMan,_eventManager, ghosts.keySet(), _labyrinth);
        SoundHandler soundHandler = new SoundHandler(this);

        _eventManager.registerObserver(InitEvent.class, gameLogic.InitGameListener);
        _eventManager.registerObserver(EnergizerEndsEvent.class, gameLogic.EnergizerEndsListener);
        _eventManager.registerObserver(EnergizerEatenEvent.class, gameLogic.EnergizerStartsListener);
        _eventManager.registerObserver(EnergizerWillBeRunningOutEvent.class, gameLogic.EnergizerWillBeRunningOutListener);

        _eventManager.registerObserver(DotEatenEvent.class, DotEatenListener);
        _eventManager.registerObserver(EnergizerEatenEvent.class, EnergizerEatenListener);

        _eventManager.registerObserver(PacManDeadEvent.class, PacManDeadListener);
        _eventManager.registerObserver(PacManDirectionRequestEvent.class, new InputHandler(_pacMan).DirectionChangedListener);
        _eventManager.registerObserver(DotEatenEvent.class, soundHandler.PlaySoundForEatingADot);
        _eventManager.registerObserver(EnergizerEatenEvent.class, soundHandler.PlaySoundForEatingAnEnergizer);
        _eventManager.registerObserver(EnergizerEatenEvent.class, EnergizerStartsListener);
        _eventManager.registerObserver(LevelCompleteEvent.class, soundHandler.LevelCompleteListener);
        _eventManager.registerObserver(PacManDeadEvent.class, soundHandler.PacManDeadListener);
        _eventManager.registerObserver(EnergizerWillBeRunningOutEvent.class, EnergizerWillBeRunningOutListener);
        _eventManager.registerObserver(GhostEatenEvent.class, GhostEatenEventListener);
        _eventManager.registerObserver(EnergizerEndsEvent.class, EnergizerEndsListener);
        _eventManager.registerObserver(LevelCompleteEvent.class, LevelCompleteHandler);

        _childViews = new ArrayList<>();
        _childViews.add(new LabyrinthView(_labyrinth, getResources()));
        _childViews.addAll(ghosts.values());
        _childViews.add(pacManView);

        return gameLogic;
    }

    private void initViews() {
        Fonts.setRegularFont(this, R.id.high_score_label);
        Fonts.setRegularFont(this, R.id.high_score_value);
        Fonts.setRegularFont(this, R.id.score_value);
        Fonts.setRegularFont(this, R.id.ouchTextView);

        GameplayView gameplayView = (GameplayView) findViewById(R.id.gameplay_view);
        gameplayView.init(_eventManager, _childViews);
        setScoreView();
        intHighScoreView();
    }

    private void intHighScoreView() {
        HighScoreRepository hr = new HighScoreRepository();
        TextView high_score_value = (TextView) findViewById(R.id.high_score_value);
        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int hs = hr.getHighScore(sharedPreferences);
        high_score_value.setText("" + hs);
    }

    public EventListener<PacManDeadEvent> PacManDeadListener = new LevelChangeEventListener<PacManDeadEvent>(this){
        @Override
        public void onEvent(PacManDeadEvent event) {
            _state.decrementLives();
            setInfoLabel(String.format("Lives: %s", _state.getLives()), Color.RED);
            changeLevelDelayed(new Runnable() {
                @Override
                public void run() {
                    setInfoLabel("", Color.RED);
                    _labyrinth.resetAllCharacters();
                    // TODO move to the GameLogic
                    for (Ghost g : _ghosts) {
                        g.init();
                        g.setMoveStrategy(new RandomMoveStrategy(_labyrinth));
                    }
                    _pacMan.init();
                    _pacMan.setMoveStrategy(new PacManMoveStrategy(_labyrinth));
                }
            });
            if (_state.getLives() == 0){
                handleGameOver();
                _frameLoop.stop();
                _state.resetCurrentLevel();
            }

        }

        private void handleGameOver() {
            GameplayView gameplayView = (GameplayView) findViewById(R.id.gameplay_view);
            gameplayView.setGameIsOver();
            // TODO: reset score
            // if high score is reached give the user the possibility to save the name
            // on the high score list.
            // _state.resetCurrentLevel();
            // _score = 0;

            gameplayView.invalidate();
        }
    };

    public EventListener<LevelCompleteEvent> LevelCompleteHandler = new LevelChangeEventListener<LevelCompleteEvent>(this) {
        @Override
        public void onEvent(LevelCompleteEvent event) {
            _frameLoop.stop();
            _state.incrementCurrentLevel();
            showNextLevelFragment(_state.getCurrentLevel());
            changeLevelDelayed(new Runnable() {
                public void run() {
                    hideNextLevelFragment();
                    _labyrinth.load(_state.getNewLabyrinthState());
                    _eventManager.fire(new InitEvent(_labyrinth.getBounds()));
                    _frameLoop.start();
                }
            });
        }

        private void hideNextLevelFragment() {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment nextLevelFragment = fragmentManager.findFragmentByTag("NEXT_LEVEL_FRAGMENT");
            fragmentTransaction.remove(nextLevelFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }

        private void showNextLevelFragment(int levelNum) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            NextLevelFragment nextLevelFragment = NextLevelFragment.newInstance(levelNum);
            fragmentTransaction.add(R.id.topmost_layout, nextLevelFragment, "NEXT_LEVEL_FRAGMENT");
            fragmentTransaction.commitAllowingStateLoss();
        }
    };

    State _state = new State();

    class State {
        public static final String RESUME_ACTION = "RESUME";
        private static final String SETTINGS = "SETTINGS";
        private static final String LABYRINTH_STATE = "LABYRINTH_STATE";
        private static final String SCORE = "SCORE";
        private static final String CURRENT_LEVEL = "CURRENT_LEVEL";
        private static final String LIVES = "LIVES";

        private String _labyrinthState;
        private int _score;
        private int _currentLevel = 1;
        private int _lives = 3;

        public String getLabyrinthState() {
            return _labyrinthState;
        }

        public String getNewLabyrinthState(){
            return getResources().getString(R.string.level_classic);
        }

        public int getScore() {
            return _score;
        }
        public int getCurrentLevel() { return _currentLevel; }
        public int getLives() { return _lives; }
        public void incrementCurrentLevel() { _currentLevel++; }
        public void decrementLives() { _lives--; }

        private void load() {
            Intent intent = getIntent();
            String action = intent.getAction();
            _labyrinthState = getNewLabyrinthState();
            if (RESUME_ACTION.equals(action)) {
                SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
                _labyrinthState = settings.getString(LABYRINTH_STATE, _labyrinthState);
                _score = settings.getInt(SCORE, _score);
                _currentLevel = settings.getInt(CURRENT_LEVEL, 1);
                _lives = settings.getInt(LIVES, _lives);
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
            editor.putInt(CURRENT_LEVEL, _currentLevel);
            editor.putInt(LIVES, _lives);
            editor.apply();
        }

        public void resetCurrentLevel() {
            _currentLevel = 1;
            _score = 0;
            _lives = 3;
        }
    }
}