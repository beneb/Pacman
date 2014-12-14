package com.example.pac.pacman.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.pac.pacman.R;

import java.util.ArrayList;

public class HighScoresActivity extends ActionBarActivity {
    private final String HIGHSCORES_PREFERENCES = "HIGHSCORES_PREFERENCES";

    ListView listView;
    private HighScoreRepository _highScoreRepository = new HighScoreRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadHighScoresList();

        final int score = getIntent().getIntExtra("high_score_value", 0);

        if (score > 0){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HighScoresActivity.this);

            alertDialog.setTitle("HIGH SCORE");

            alertDialog.setMessage("Do you like to pu your name in the highscores list?");

            final EditText input = new EditText(HighScoresActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            input.setLayoutParams(lp);
            alertDialog.setView(input);

            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            _highScoreRepository.addScore(score, input.getText().toString(), sharedPreferences);
                            LoadHighScoresList();
                            dialog.cancel();
                        }
                    });
            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }


        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO: could be possible to show some details here like time or so?

            }

        });
    }

    private void LoadHighScoresList() {
        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ArrayList<String> highScores = _highScoreRepository.getScoresStrings(sharedPreferences);

        if (highScores.isEmpty()){
            highScores.add("Jojo: 1");
        }

        setContentView(R.layout.activity_high_scores);
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listViewHighScores);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, highScores);


        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.high_scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
