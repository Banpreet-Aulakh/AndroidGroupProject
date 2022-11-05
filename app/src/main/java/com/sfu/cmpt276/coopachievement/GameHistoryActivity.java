package com.sfu.cmpt276.coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import com.sfu.cmpt276.coopachievement.model.GameConfig;
import com.sfu.cmpt276.coopachievement.model.GameHistory;
import com.sfu.cmpt276.coopachievement.model.GamePlayed;
import com.sfu.cmpt276.coopachievement.model.Singleton;

/*
The GameHistoryActivity Activity is responsible for displaying the instances of GamesPlayed in the GameHistory Class in a List Format. This is shown after a user
selects the game configuration in the previous screen's menu.
 */

public class GameHistoryActivity extends AppCompatActivity {

    private final static String positionCodeName = "POSITION";
    private int position;
    private GameHistory gameHistory;
    private Singleton singleton;
    private GameConfig gameConfig;
    private ActionBar ab;

    //Gets the position extra for editing game config
    public static Intent getIntent(Context context, int position){
        Intent intent = new Intent(context, GameHistoryActivity.class);
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

        getDataFromIntent();

        singleton = Singleton.getInstance();
        gameConfig = singleton.getGameConfigList().get(position);
        gameHistory = gameConfig.getGameHistory();
        gameHistory.setConfigName(gameConfig.getGameName());

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Floating Action Button
        floatingActionButton();

        //STUB GAME HISTORY CODE
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
        singleton = Singleton.getInstance();
        gameConfig = singleton.getGameConfigList().get(position);
        gameHistory = gameConfig.getGameHistory();
        gameHistory.setConfigName(gameConfig.getGameName());
        ab.setTitle(gameHistory.getConfigName() + " History");
    }

    private void floatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.add_new_game_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = NewGameActivity.makeIntent(GameHistoryActivity.this);
               intent.putExtra("configIndex", position);
               startActivity(intent);
            }
        });
    }

    private void populateListView(GameHistory gameHistory) {
        String[] items = gameHistory.getStringArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.game_history,items);
        ListView list = findViewById(R.id.game_history_list);
        list.setAdapter(adapter);
    }

//Will contain code to handle editing game instance
    private void listOnClick() {
        ListView list = findViewById(R.id.game_history_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                TextView textView = (TextView) view;

                //go to edit game activity (new game activity with extra)
                Intent intent = NewGameActivity.makeIntent(GameHistoryActivity.this);
                intent.putExtra("historyIndex", index);
                intent.putExtra("configIndex", position);
                startActivity(intent);
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