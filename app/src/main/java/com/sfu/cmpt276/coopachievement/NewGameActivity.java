package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class NewGameActivity extends AppCompatActivity {
    //private GameType Game;
    //private GameHistoryList List;
    private EditText numPlayers;
    private EditText totalScore;
    private TextView displayAchievementText;
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        Intent intent = getIntent();
        this.index = intent.getIntExtra("index", -1);
        numPlayers = findViewById(R.id.numPlayersEditText);
        totalScore = findViewById(R.id.totalScoreEditText);
        //Game = new Game();
        //How will I access the list of games
        //List ????
        ActionBar toolbar = getSupportActionBar();

        if(index != -1){
            //numPlayers.setText(//""+Game.getNumPlayers(), TextView.BufferType.EDITABLE);
            //totalScore.setText(//??+Game.getNumPlayers(), TextView.BufferType.EDITABLE);
            toolbar.setTitle("Edit Game");

        }else{
            toolbar.setTitle("New Game");
        }


    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, NewGameActivity.class);
    }

    private int getIntFromTextview(TextView input){
        String value = input.getText().toString();
        return Integer.parseInt(value);
    }

}