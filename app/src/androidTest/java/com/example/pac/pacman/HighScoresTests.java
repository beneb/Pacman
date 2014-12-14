package com.example.pac.pacman;

import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import com.example.pac.pacman.activities.HighScoreRepository;
import java.util.ArrayList;

public class HighScoresTests extends ActivityInstrumentationTestCase2 {
    private HighScoreRepository _highScoreRepository = new HighScoreRepository();

    public HighScoresTests(){
        super(null);
    }

    @Override
    public void setUp() throws Exception {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.apply();
    }

    public void test_highscores_are_empty_at_the_begin() {
        ArrayList<String> highScores =
                _highScoreRepository.getScoresStrings(getSharedPreferences());
        assertEquals(0, highScores.size());
    }
    public void test_only_one_high_score() {
        _highScoreRepository.addScore(10, "aaaa", getSharedPreferences());
        ArrayList<String> highScores =
                _highScoreRepository.getScoresStrings(getSharedPreferences());
        assertEquals(1, highScores.size());
        assertEquals("aaaa: 10", highScores.get(0));
    }

    public void test_second_after_first() {
        _highScoreRepository.addScore(15, "bbbb", getSharedPreferences());
        _highScoreRepository.addScore(10, "aaaa", getSharedPreferences());
        ArrayList<String> highScores =
                _highScoreRepository.getScoresStrings(getSharedPreferences());
        assertEquals(2, highScores.size());
        assertEquals("bbbb: 15", highScores.get(0));
        assertEquals("aaaa: 10", highScores.get(1));
    }

    public void test_second_before_first() {
        _highScoreRepository.addScore(5, "bbbb", getSharedPreferences());
        _highScoreRepository.addScore(10, "aaaa", getSharedPreferences());
        ArrayList<String> highScores =
                _highScoreRepository.getScoresStrings(getSharedPreferences());
        assertEquals(2, highScores.size());
        assertEquals("aaaa: 10", highScores.get(0));
        assertEquals("bbbb: 5", highScores.get(1));
    }
    public void test_only_ten_highscores_are_allowed() {
        for (int i = 0; i < 42; i++){
            _highScoreRepository.addScore(i, "a" + i, getSharedPreferences());
        }

        ArrayList<String> highScores =
                _highScoreRepository.getScoresStrings(getSharedPreferences());
        assertEquals(10, highScores.size());
    }

    public void test_new_high_score_first_place() {
        for (int i = 0; i < 42; i++){
            _highScoreRepository.addScore(100, "a", getSharedPreferences());
        }

        _highScoreRepository.addScore(101, "b",getSharedPreferences());

        ArrayList<String> highScores =
                _highScoreRepository.getScoresStrings(getSharedPreferences());
        assertEquals("b: 101", highScores.get(0));
        assertEquals("a: 100", highScores.get(1));
        assertEquals("a: 100", highScores.get(9));
    }
    public void test_new_high_score_last_place() {
        for (int i = 0; i < 9; i++){
            _highScoreRepository.addScore(10, "a", getSharedPreferences());
        }

        _highScoreRepository.addScore(8, "b",getSharedPreferences());
        _highScoreRepository.addScore(9, "x",getSharedPreferences());

        ArrayList<String> highScores =
                _highScoreRepository.getScoresStrings(getSharedPreferences());
        for (int i = 0; i < 9; i++) {
            assertEquals("a: 10", highScores.get(i));
        }
        assertEquals("x: 9", highScores.get(9));
    }

    private SharedPreferences getSharedPreferences() {
        String DUMMY = "DUMMY";
        return getInstrumentation().getTargetContext().getSharedPreferences(DUMMY, 0);
    }
}
