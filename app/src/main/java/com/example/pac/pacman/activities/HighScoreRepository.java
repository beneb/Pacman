package com.example.pac.pacman.activities;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoreRepository {
    private final String HIGHSCORES = "HighScores";

    private ArrayList<Score> getScores(SharedPreferences sharedPreferences) {
        String highJson = sharedPreferences.getString(HIGHSCORES, "");
        Gson gson = new Gson();
        ScoreList result = gson.fromJson(highJson, ScoreList.class);

        if (result == null){
            return new ArrayList<Score>();
        }

        return result.scores;
    }
    public ArrayList<String> getScoresStrings(SharedPreferences sharedPreferences) {
        ArrayList<Score> highScores = getScores(sharedPreferences);

        ArrayList<String> result = new ArrayList<String>();
        if (!highScores.isEmpty()) {
            for (Score hs : highScores) {
                result.add(hs.name + ": " + hs.score);
            }
        }
        result.add("Jojo - dummy: 42");
        return result;
    }

    public boolean isHighScore(int candidate, SharedPreferences sharedPreferences) {
        ArrayList<Score> highScores = getScores(sharedPreferences);
        if (!highScores.isEmpty()) {
            for (Score hs : highScores) {
                if (candidate > hs.score) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addScore(int score, String name, SharedPreferences sharedPreferences) {
        ArrayList<Score> highScores = getScores(sharedPreferences);
        Score s = new Score();
        s.score = score;
        s.name = name;
        highScores.add(s);

        Collections.sort(highScores);

        // cut the list to 10 only
        int k = highScores.size();
        if ( k > 10) highScores.subList(10, k).clear();

        ScoreList scoreList = new ScoreList();
        scoreList.scores = highScores;

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(scoreList);
        editor.putString(HIGHSCORES, json);
        editor.commit();
    }


    class ScoreList {
        public ArrayList<Score> scores = new ArrayList<Score>();
    }

    class Score implements Comparable<Score> {

        public String name;
        public int score;


        @Override
        public int compareTo(Score another) {
            if (score == another.score){
                return 0;
            }

            if (score < another.score){
                return -1;
            }

            return 1;
        }
    }
}
