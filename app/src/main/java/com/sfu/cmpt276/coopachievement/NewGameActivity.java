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
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sfu.cmpt276.coopachievement.model.GameConfig;
import com.sfu.cmpt276.coopachievement.model.GamePlayed;
import com.sfu.cmpt276.coopachievement.model.Singleton;

/*
 * The NewGame Activity is responsible for taking user's total score input and number of player
 * before dynamically printing out the achievement level that the user acquired.
 */

public class NewGameActivity extends AppCompatActivity {
    final static  private int EASY = 0;
    final static private int MEDIUM = 1;
    final static private int HARD = 2;
    private Singleton configList;
    private GameConfig gameConfiguration;
    private GamePlayed currentGame;
    private EditText numPlayers;
    private EditText totalScore;
    private TextView displayAchievementText;
    private int historyIndex;
    private int configIndex;
    private int selectedDifficultyButton;

    private String [] achievementsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);


        configList = Singleton.getInstance();
        achievementsList = new String[]{
                getResources().getString(R.string.lowly_leech),
                getResources().getString(R.string.horrendous_hagfish),
                getResources().getString(R.string.bogus_blowfish),
                getResources().getString(R.string.terrible_trolls),
                getResources().getString(R.string.goofy_goblins),
                getResources().getString(R.string.dastardly_dragons),
                getResources().getString(R.string.awesome_alligators),
                getResources().getString(R.string.epic_elephants),
                getResources().getString(R.string.fabulous_fairies)};

        Intent intent = getIntent();
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
        RadioGroup difficultyRadioGroup = findViewById(R.id.difficultyRadioGroup);
        setupDifficultyRadioButtons();

        //history index default to -1 for new game, otherwise is index of game we are editing
        if(historyIndex != -1){
            //get the specific game played that was clicked on
            this.currentGame = gameConfiguration.getGameHistory().getGameHistoryList().get(historyIndex);
            numPlayers.setText(""+currentGame.getNumPlayers(), TextView.BufferType.EDITABLE);
            totalScore.setText(""+currentGame.getTotalScore(), TextView.BufferType.EDITABLE);
            toolbar.setTitle(R.string.edit_game);

            //set radio group button as checked
            RadioButton button = (RadioButton) difficultyRadioGroup.getChildAt(currentGame.getDifficulty());
            button.setChecked(true);

        }else{
            toolbar.setTitle(R.string.new_game);
            this.currentGame = new GamePlayed();
            //Index 1: medium difficulty by default
            RadioButton button = (RadioButton) difficultyRadioGroup.getChildAt(MEDIUM);
            button.setChecked(true);
            selectedDifficultyButton = MEDIUM;
        }
    }

    private void setupDifficultyRadioButtons() {
        RadioGroup group = findViewById(R.id.difficultyRadioGroup);

        String[] difficulties = getResources().getStringArray(R.array.difficulty_settings);

        for(int i = 0; i < difficulties.length; i++){
            String difficulty = difficulties[i];

            RadioButton button = new RadioButton(this);
            button.setText(difficulty);

            int difficultySetting = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedDifficultyButton = difficultySetting;
                    String gameTotalScore = totalScore.getText().toString().trim();
                    String gameNumPlayers = numPlayers.getText().toString().trim();
                    if (!gameTotalScore.isEmpty() && !gameNumPlayers.isEmpty() && getIntFromEditText(numPlayers) != 0) {
                        int numberPlayers = getIntFromEditText(numPlayers);
                        int combinedScore = getIntFromEditText(totalScore);
                        gameConfiguration.setAchievement_Thresholds(selectedDifficultyButton);
                        displayAchievementText.setText(currentGame.checkAchievementLevel(gameConfiguration.getAchievement_Thresholds(), achievementsList, numberPlayers, combinedScore));
                        displayAchievementLevel();
                    }else{
                        displayAchievementText.setText(getResources().getString(R.string.empty_string));
                    }
                }
            });
            group.addView(button);
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
                    currentGame.setNumPlayers(getIntFromEditText(numPlayers));
                    currentGame.setTotalScore(getIntFromEditText(totalScore));
                    currentGame.setDifficulty(selectedDifficultyButton);
                    gameConfiguration.setAchievement_Thresholds(selectedDifficultyButton);
                    currentGame.setAchievementLevel(gameConfiguration.getAchievement_Thresholds(), achievementsList);

                    if (historyIndex != -1) {
                        gameConfiguration.getGameHistory().setGamePlayed(historyIndex, currentGame);
                    }
                    else {
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

    public static Intent makeIntent(Context context) {
        return new Intent(context, NewGameActivity.class);
    }

    private int getIntFromEditText(TextView input){
        String value = input.getText().toString();
        return Integer.parseInt(value);
    }


    //helper function to show the correct achievement level to screen
    private void displayAchievementLevel(){
        String gameTotalScore = totalScore.getText().toString().trim();
        String gameNumPlayers = numPlayers.getText().toString().trim();

        if (!gameTotalScore.isEmpty() && !gameNumPlayers.isEmpty() && getIntFromEditText(numPlayers) != 0) {
            int numberPlayers = getIntFromEditText(numPlayers);
            int combinedScore = getIntFromEditText(totalScore);
            gameConfiguration.setAchievement_Thresholds(selectedDifficultyButton);
            displayAchievementText.setText(currentGame.checkAchievementLevel(gameConfiguration.getAchievement_Thresholds(), achievementsList, numberPlayers, combinedScore));

        }else{
            displayAchievementText.setText(getResources().getString(R.string.empty_string));
        }
    }


    private TextWatcher checkFinished = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            displayAchievementLevel();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}