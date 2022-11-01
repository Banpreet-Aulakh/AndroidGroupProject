package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

public class NewGame extends AppCompatActivity {
    private GamePlayed currentGame;
    private EditText numPlayers;
    private EditText totalScore;
    private TextView displayAchievementText;
    //private gameConfigurations config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);


        numPlayers = findViewById(R.id.numPlayersEditText);
        totalScore = findViewById(R.id.totalScoreEditText);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }

    public Intent createIntent(Context context){
        return new Intent(context, GamePlayed.class);
    }

    private int getIntFromTextview(TextView input){
        String value = input.getText().toString();
        return Integer.parseInt(value);
    }

}