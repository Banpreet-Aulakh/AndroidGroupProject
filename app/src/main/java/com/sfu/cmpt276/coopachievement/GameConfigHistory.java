package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/*
The GameConfigHistory Activity is responsible for displaying the instances of GamesPlayed in the GameHistory Class in a List Format. This is shown after a user
selects the game configuration in the previous screen's menu.
 */
public class GameConfigHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config_history);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.game_history_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.add_new_game_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameConfigHistory.this, MainActivity.class); //STUB code for changing activities on FAB
                GameConfigHistory.this.startActivity(intent);
            }
        });
    }

}