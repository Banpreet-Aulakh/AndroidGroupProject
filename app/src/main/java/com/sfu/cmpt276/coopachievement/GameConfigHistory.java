package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sfu.cmpt276.coopachievement.model.GameHistory;
import com.sfu.cmpt276.coopachievement.model.GamesPlayed;

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

        //Toolbar
        myToolbar.setTitle("Game History");
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Floating Action Button

        floatingActionButton();

        //STUB GAME HISTORY CODE
        GameHistory gameHistory = new GameHistory(new String("Test"));
        for (int i = 0; i < 10; i++){
            gameHistory.getGameHistory().add(new GamesPlayed());
        }

        //ListView
        populateListView(gameHistory);
        listOnClick();
    }

    private void floatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.add_new_game_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameConfigHistory.this, MainActivity.class); //STUB code for changing activities on FAB
                GameConfigHistory.this.startActivity(intent);
            }
        });
    }

    private void populateListView(GameHistory gameHistory) {
        String[] items = gameHistory.getStringArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.game_history,items);
        ListView list = findViewById(R.id.game_history_list);
        list.setAdapter(adapter);
    }

    private void listOnClick() {
        ListView list = findViewById(R.id.game_history_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                String message = "You clicked " + i + " which is string: " + textView.getText().toString();
                Toast.makeText(GameConfigHistory.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}