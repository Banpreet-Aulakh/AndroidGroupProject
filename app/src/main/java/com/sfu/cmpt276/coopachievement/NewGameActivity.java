package com.sfu.cmpt276.coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sfu.cmpt276.coopachievement.model.GameConfig;
import com.sfu.cmpt276.coopachievement.model.GamePlayed;
import com.sfu.cmpt276.coopachievement.model.Singleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * The NewGame Activity is responsible for taking user's total score input and number of player
 * before dynamically printing out the achievement level that the user acquired.
 */

public class NewGameActivity extends AppCompatActivity {
    private final int ACHIEVEMENT_LIST_SIZE = 8;
    private Singleton configList;
    private GameConfig gameConfiguration;
    private GamePlayed currentGame;
    private EditText numPlayers;
    private EditText totalScore;
    private TextView displayAchievementText;
    private int historyIndex;
    private int configIndex;

    private String[] achievementsList;

    //testing sharedpreference in option
    public static final String EXTRA_THEME = "Theme position in Option";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        configList = Singleton.getInstance();

//        achievementsList = new String[]{
//                getResources().getString(R.string.lowly_leech),
//                getResources().getString(R.string.horrendous_hagfish),
//                getResources().getString(R.string.bogus_blowfish),
//                getResources().getString(R.string.terrible_trolls),
//                getResources().getString(R.string.goofy_goblins),
//                getResources().getString(R.string.dastardly_dragons),
//                getResources().getString(R.string.awesome_alligators),
//                getResources().getString(R.string.epic_elephants),
//                getResources().getString(R.string.fabulous_fairies)};

        //set up for changing achievements based on Option theme
        Intent intent = getIntent();
        int themeIndex = configList.getThemeIndex();
        final int FINAL_THEME_INDEX = themeIndex;
        if (FINAL_THEME_INDEX ==0)
        {
            achievementsList = populateAchievement();
        }
        if (FINAL_THEME_INDEX ==1)
        {
            achievementsList = populateAchievement();
        }
        if(FINAL_THEME_INDEX==2)
        {
            achievementsList = populateAchievement();
        }
//end of test

        configIndex = intent.getIntExtra("configIndex", -1);
        historyIndex = intent.getIntExtra("historyIndex", -1);

        numPlayers = findViewById(R.id.numPlayersEditText);
        totalScore = findViewById(R.id.totalScoreEditText);
        displayAchievementText = findViewById(R.id.displayAchievementText);

        numPlayers.addTextChangedListener(checkFinished);
        totalScore.addTextChangedListener(checkFinished);


        gameConfiguration = configList.getGameConfigList().get(configIndex);
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);

        //history index default to -1 for new game, otherwise is index of game we are editing
        if(historyIndex != -1){
            //get the specific game played that was clicked on
            this.currentGame = gameConfiguration.getGameHistory().getGameHistoryList().get(historyIndex);
            numPlayers.setText(""+currentGame.getNumPlayers(), TextView.BufferType.EDITABLE);
            totalScore.setText(""+currentGame.getTotalScore(), TextView.BufferType.EDITABLE);
            toolbar.setTitle(R.string.edit_game);

        }else{
            toolbar.setTitle(R.string.new_game);
            this.currentGame = new GamePlayed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_game_played, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.saveGame:
                if(totalScore.getText().toString().isEmpty() || numPlayers.getText().toString().isEmpty()){
                    Toast.makeText(NewGameActivity.this, "Error: Empty game elements", Toast.LENGTH_LONG).show();
                }else if(getIntFromEditText(numPlayers) == 0){

                    Toast.makeText(NewGameActivity.this, getString(R.string.zero_error),Toast.LENGTH_LONG).show();

                }else{
                    if (historyIndex != -1) {
                        currentGame.setNumPlayers(getIntFromEditText(numPlayers));
                        currentGame.setTotalScore(getIntFromEditText(totalScore));
                        gameConfiguration.getGameHistory().setGamePlayed(historyIndex, currentGame);
                    }
                    else {
                        currentGame.setNumPlayers(getIntFromEditText(numPlayers));
                        currentGame.setTotalScore(getIntFromEditText(totalScore));
                        gameConfiguration.getGameHistory().addPlayedGame(currentGame);
                    }
                    ViewConfigListActivity.saveData(NewGameActivity.this);
                    finish();
                }
                return true;
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //test function to populate achievement array
    public String []populateAchievement(){
        String[] list = new String[9];
        for(int i = 0 ; i<9;i++)
        {
            list[i] = getResources().getStringArray(R.array.mythical)[i];
        }
        return list;
    }
//end of test

    public static Intent makeIntent(Context context) {
        return new Intent(context, NewGameActivity.class);
    }

    private int getIntFromEditText(TextView input){
        String value = input.getText().toString();
        return Integer.parseInt(value);
    }
    private TextWatcher checkFinished = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String gameTotalScore = totalScore.getText().toString().trim();
            String gameNumPlayers = numPlayers.getText().toString().trim();

            if (!gameTotalScore.isEmpty() && !gameNumPlayers.isEmpty() && getIntFromEditText(numPlayers) != 0) {
                int numberPlayers = getIntFromEditText(numPlayers);
                int combinedScore = getIntFromEditText(totalScore);
                displayAchievementText.setText(currentGame.checkAchievementLevel(gameConfiguration.getAchievement_Thresholds(), achievementsList, numberPlayers, combinedScore));

            }else{
                displayAchievementText.setText(getResources().getString(R.string.empty_string));
            }
        }



        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}