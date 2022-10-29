package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/*
The GameConfigHistory Activity is responsible for displaying the instances of GamesPlayed in the GameHistory Class in a List Format. This is shown after a user
selects the game configuration in the previous screen's menu.
 */
public class GameConfigHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config_history);
    }
}