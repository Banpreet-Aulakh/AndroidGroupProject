package com.sfu.cmpt276.coopachievement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sfu.cmpt276.coopachievement.model.GameConfig;
import com.sfu.cmpt276.coopachievement.model.GameHistory;
import com.sfu.cmpt276.coopachievement.model.Singleton;

import java.util.ArrayList;

/*
 * The GameHistory Activity is responsible for displaying the instances of GamesPlayed
 * in the GameHistory Class in a List Format.This is shown after a user selects the game configuration
 * in the previous screen's menu.
 */

public class GameHistoryActivity extends AppCompatActivity {
    //private final int ACHIEVEMENT_LIST_SIZE = 8;
    private final static String positionCodeName = "POSITION";
    private int position;
    private GameHistory gameHistory;
    private Singleton singleton;
    private GameConfig gameConfig;
    private ActionBar ab;
    private String [] achievementsList;
    private TextView noItemView;
    private ArrayList<String[]> paramsList;
    private Button statButton;

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
        noItemView = findViewById(R.id.no_items_history_text);
        noItemView.setVisibility(View.INVISIBLE);

        statButton = findViewById(R.id.btn_achievement_stats);
        statButton.setVisibility(View.INVISIBLE);

        singleton = Singleton.getInstance();
        int themeIndex = singleton.getThemeIndex();
        achievementsList = populateAchievementList(themeIndex);

        getDataFromIntent();

        singleton = Singleton.getInstance();
        gameConfig = singleton.getGameConfigList().get(position);
        gameHistory = gameConfig.getGameHistory();
        gameHistory.setConfigName(gameConfig.getGameName());

        if(gameHistory.getGameHistoryList().size() == 0){
            noItemView.setVisibility(View.VISIBLE);
        }
        else{
            statButton.setVisibility(View.VISIBLE);
        }
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Floating Action Button
        floatingActionButton();
        
        //Statistics Button
        statisticButton();

        //STUB GAME HISTORY CODE
        String configName = gameHistory.getConfigName();
        ab.setTitle(configName + " History");

        //ListView
        if(gameHistory.getGameHistoryList().size() > 0){
            populateListView(gameHistory);
            listOnClick();
        }
    }

    private void statisticButton() {
        statButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle varBundle = new Bundle();
                varBundle.putIntArray("counter", gameConfig.getAchievementCounter());
                varBundle.putStringArray("achievementList", achievementsList);

                StatFragment statFragment = new StatFragment();
                statFragment.setArguments(varBundle);
                statFragment.show(getSupportFragmentManager(), "Statistics");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int themeIndex = singleton.getThemeIndex();
        achievementsList = populateAchievementList(themeIndex);
        updateGameAchievements();
        if(gameHistory.getGameHistoryList().size() > 0) {
            populateListView(gameHistory);
            listOnClick();
        }
        noItemView.setVisibility(View.INVISIBLE);
        if(gameHistory.getGameHistoryList().size() == 0){
            noItemView.setVisibility(View.VISIBLE);
        }
        else {
            statButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateGameAchievements() {
        singleton = Singleton.getInstance();
        gameConfig = singleton.getGameConfigList().get(position);
        gameHistory = gameConfig.getGameHistory();
        gameHistory.setConfigName(gameConfig.getGameName());
        ab.setTitle(gameHistory.getConfigName() + " History");

        for(int i = 0; i < gameHistory.getGameHistoryList().size(); i++){
            gameConfig.setAchievement_Thresholds(gameHistory.getGameHistoryList().get(i).getDifficulty());
            gameHistory.getGameHistoryList().get(i).setAchievementLevel(gameConfig.getAchievement_Thresholds(), achievementsList);
        }
    }

    private String[] populateAchievementList(int themeIndex) {

        if(themeIndex== 0){
            String[] themeArray=getResources().getStringArray(R.array.mythical);
            return themeArray;
        }
        if (themeIndex==1)
        {
            String[] themeArray=getResources().getStringArray(R.array.paw_patrol);
            return themeArray;

        }
        if(themeIndex==2)
        {
            String[] themeArray=getResources().getStringArray(R.array.dinosaur);
            return themeArray;
        }
        else
            return achievementsList;
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
        paramsList = gameHistory.getParamsArrayList();
        ArrayAdapter<String[]> adapter = new MyListAdapter();
        ListView list = findViewById(R.id.game_history_list);
        list.setAdapter(adapter);
    }

    //Will contain code to handle editing game instance
    private void listOnClick() {
        ListView list = findViewById(R.id.game_history_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
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

    private class MyListAdapter extends ArrayAdapter<String[]> {

        public MyListAdapter(){
            super(GameHistoryActivity.this, R.layout.game_history_list, paramsList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.game_history_list, parent, false);
            }
            if(!paramsList.isEmpty()) {
                String[] currentParams = paramsList.get(position);

                TextView totalScore = itemView.findViewById(R.id.total_score_history_param);
                totalScore.setText(currentParams[0]+"");

                TextView numPlayer = itemView.findViewById(R.id.num_players_history_param);
                numPlayer.setText(currentParams[1]+"");

                TextView difficultyLevel = itemView.findViewById(R.id.difficulty_history_param);
                difficultyLevel.setText(currentParams[2]+"");

                TextView achievement = itemView.findViewById(R.id.achievement_history_param);
                achievement.setText(currentParams[3]+"");

                TextView time = itemView.findViewById(R.id.time_history_param);
                time.setText(currentParams[4]+"");

                TextView gameNumber = itemView.findViewById(R.id.game_number);
                gameNumber.setText("Game " + (position+1));
            }
            return itemView;
        }


    }
}
