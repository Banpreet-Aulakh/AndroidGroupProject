package com.sfu.cmpt276.coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    private final static String positionCodeName = "POSITION";
    private int position;
    private GameHistory gameHistory;

    public static Intent getIntent(Context context, int position){
        Intent intent = new Intent(context, GameConfigHistory.class);
        intent.putExtra(positionCodeName, position);
        return intent;
    }

    private void getDataFromIntent(){
        Intent intent = getIntent();
        position = intent.getIntExtra(positionCodeName, -1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config_history);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Floating Action Button
        floatingActionButton();

        //STUB GAME HISTORY CODE
        gameHistory = new GameHistory(new String("Test")); //This will change its name to whatever is saved in config
        String configName = gameHistory.getConfigName();
        ab.setTitle(configName + " History");

        //ListView
        populateListView(gameHistory);
        listOnClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateGameAchievements();
        populateListView(gameHistory);
    }

    private void updateGameAchievements() {
        /*
        Stub method will use the Game instance model methods to update achievements based on config
        */
        gameHistory.getGameHistory().clear();
        for (int i = 0; i < 10; i++){
            gameHistory.getGameHistory().add(new GamesPlayed());
        }
    }

    private void floatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.add_new_game_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GameConfigHistory.this, "Go to Add Game Activity", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_history_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.toolbar_edit_game_config){
            Intent intent = EditConfigActivity.getIntent(this, position);
            startActivity(intent);
        }
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}