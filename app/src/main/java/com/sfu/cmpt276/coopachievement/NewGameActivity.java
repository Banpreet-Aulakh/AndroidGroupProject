package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sfu.cmpt276.coopachievement.model.GameConfig;
import com.sfu.cmpt276.coopachievement.model.GamePlayed;
import com.sfu.cmpt276.coopachievement.model.Singleton;

import java.util.ArrayList;

/*
 * The NewGame Activity is responsible for taking user's total score input and number of player
 * before dynamically printing out the achievement level that the user acquired.
 */

public class NewGameActivity extends AppCompatActivity {
    final static  private int EASY = 0;
    final static private int MEDIUM = 1;
    final static private int HARD = 2;
    private GameConfig gameConfiguration;
    private GamePlayed currentGame;
    private EditText numPlayers;
    private int numPlayersInt;
    private ArrayList<Integer> playerScoreArray;
    private TextView displayAchievementText;
    private int historyIndex;
    private int configIndex;
    private int selectedDifficultyButton;
    private ComplexAdapter complexAdapter;
    private String [] achievementsList;
    private ListView list;
    private TextView updateTotalScore;
    private boolean initialize;
    private boolean complexAdapterInitialize;
    private ArrayList<Integer> copyOriginalArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        playerScoreArray = new ArrayList<Integer>();


        Singleton configList = Singleton.getInstance();
        int themeIndex = configList.getThemeIndex();
        achievementsList = populateAchievementList(themeIndex);


        Intent intent = getIntent();
        configIndex = intent.getIntExtra("configIndex", -1);
        historyIndex = intent.getIntExtra("historyIndex", -1);

        numPlayers = findViewById(R.id.numPlayersEditText);
        displayAchievementText = findViewById(R.id.displayAchievementText);
        numPlayers.addTextChangedListener(checkFinished);
        updateTotalScore = findViewById(R.id.txtTotalScore);

        gameConfiguration = configList.getGameConfigList().get(configIndex);
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        RadioGroup difficultyRadioGroup = findViewById(R.id.difficultyRadioGroup);
        setupDifficultyRadioButtons();

        //history index default to -1 for new game, otherwise is index of game we are editing
        if (historyIndex != -1) {
            //set to true to help initialize values when populating list view
            initialize = true;
            complexAdapterInitialize = true;

            //get the specific game played that was clicked on
            this.currentGame = gameConfiguration.getGameHistory().getGameHistoryList().get(historyIndex);
            toolbar.setTitle(R.string.edit_game);
            numPlayers.setText(""+currentGame.getNumPlayers(), TextView.BufferType.EDITABLE);
            displayAchievementText.setText(currentGame.getAchievementName());
            updateTotalScore.setText(Integer.toString(currentGame.getTotalScore()));

            //set radio group button as checked
            RadioButton button = (RadioButton) difficultyRadioGroup.getChildAt(currentGame.getDifficulty());
            button.setChecked(true);

            ArrayList<Integer> tempArray = new ArrayList<>();
            for (int j = 0; j < numPlayersInt; j++) {
                tempArray.add(-1);
            }

            complexAdapter = new ComplexAdapter(NewGameActivity.this,
                    R.layout.player_score_row, tempArray);
            playerScoreArray = currentGame.getListScore();

            // original array values cloned in case of back button press cloned
            copyOriginalArray = (ArrayList<Integer>) currentGame.getListScore().clone();

            numPlayersInt = currentGame.getNumPlayers();
            currentGame.setNumPlayers(numPlayersInt);
            list = findViewById(R.id.listViewPlayers);
            list.setAdapter(complexAdapter);

        }
        else {
            toolbar.setTitle(R.string.new_game);
            initialize = false;
            this.currentGame = new GamePlayed();
            complexAdapter = new ComplexAdapter(NewGameActivity.this,
                    R.layout.player_score_row, playerScoreArray);
            //Index 1: medium difficulty by default
            RadioButton button = (RadioButton) difficultyRadioGroup.getChildAt(MEDIUM);
            button.setChecked(true);
            selectedDifficultyButton = MEDIUM;
        }
    }

    public void celebrationMessage() {
        FragmentManager manager = getSupportFragmentManager();
        MessageFragment dialog = new MessageFragment();
        dialog.show(manager, "");
        final MediaPlayer saveSound = MediaPlayer.create(NewGameActivity.this,R.raw.shouting_yeah);
        saveSound.start();

    }
    //Get Theme for Achievements
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
    //Easy Normal Hard difficulty selection
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

                    String gameNumPlayers = numPlayers.getText().toString().trim();

                    if(updateTotalScore==null){
                       updateTotalScore=findViewById(R.id.txtTotalScore);
                   }
                    if (!updateTotalScore.getText().toString().equals("-") && !gameNumPlayers.isEmpty() && getIntFromEditText(numPlayers) != 0) {
                        int numberPlayers = getIntFromEditText(numPlayers);

                        int combinedScore = currentGame.getTotalScore(); // changed for branch
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.saveGame:
                if(updateTotalScore.getText().toString().equals("-") || numPlayers.getText().toString().isEmpty()){ //changed for branch
                    Toast.makeText(NewGameActivity.this, R.string.new_game_zero_players, Toast.LENGTH_LONG).show();
                }else if(getIntFromEditText(numPlayers) == 0){
                    Toast.makeText(NewGameActivity.this, getString(R.string.zero_error),Toast.LENGTH_LONG).show();

                }else {
                    currentGame.setNumPlayers(getIntFromEditText(numPlayers));
                    currentGame.setListScore(playerScoreArray);
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
                    celebrationMessage();


                }
                return true;
            case android.R.id.home:
                if(historyIndex != -1){
                    currentGame.setNumPlayers(copyOriginalArray.size());
                    currentGame.setTotalScore(copyOriginalArray);
                }
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
        Animation scaleUp,scaleDown;
        scaleUp= AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown=AnimationUtils.loadAnimation(this,R.anim.scale_down);
        String gameNumPlayers = numPlayers.getText().toString().trim();

        if (!updateTotalScore.getText().toString().equals("-") && !gameNumPlayers.isEmpty() && getIntFromEditText(numPlayers) != 0) {
            int numberPlayers = getIntFromEditText(numPlayers);
            int combinedScore = currentGame.getTotalScore(); //Changed for branch
            gameConfiguration.setAchievement_Thresholds(selectedDifficultyButton);
            displayAchievementText.setText(currentGame.checkAchievementLevel(
                    gameConfiguration.getAchievement_Thresholds(), achievementsList, numberPlayers, combinedScore));

            displayAchievementText.startAnimation(scaleUp);
            displayAchievementText.startAnimation(scaleDown);
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
            displayAchievementText.setText(R.string.empty_string);
            if (!numPlayers.getText().toString().isEmpty()) {
                numPlayersInt = Integer.parseInt(numPlayers.getText().toString());
            } else {
                TextView updateTotalScore = findViewById(R.id.txtTotalScore);
                updateTotalScore.setText(R.string.empty_string);
                numPlayersInt = 0;
                list = findViewById(R.id.listViewPlayers);
                list.setAdapter(null);
            }
            if(numPlayersInt != 0){
                if(!initialize) {
                    playerScoreArray.clear();
                    for (int j = 0; j < numPlayersInt; j++) {
                        playerScoreArray.add(-1);
                    }
                    currentGame.setNumPlayers(numPlayersInt);
                    list = findViewById(R.id.listViewPlayers);
                    list.setAdapter(complexAdapter);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    private class ComplexAdapter extends ArrayAdapter<Integer> {
        private Context contextMain;
        private int resourceLayout;

        public ComplexAdapter(Context context, int resourceLayout, ArrayList<Integer> values) {
            super(context, resourceLayout, values);
            this.contextMain = context;
            this.resourceLayout = resourceLayout;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(contextMain);
                itemView = inflater.inflate(resourceLayout, parent, false);
            }

            TextView playerText = itemView.findViewById(R.id.playerNumber);
            EditText getScore = itemView.findViewById(R.id.getPlayerScore);

            playerText.setText("Player " + (position+1) + " Score:");

            updateTotalScore = (TextView) ((Activity)contextMain).findViewById(R.id.txtTotalScore);

            //Helper code for initializing values in case of editing array

            if(historyIndex != -1 && initialize){
                getScore.setText(currentGame.getListScore().get(position)+"");
                if(position == 3){
                    updateTotalScore.setText(currentGame.getTotalScore()+"");
                    initialize = false;
                    complexAdapter = new ComplexAdapter(NewGameActivity.this,
                            R.layout.player_score_row, playerScoreArray);
                }
            }
            if(playerScoreArray.get(position) != -1){
                getScore.setText(currentGame.getListScore().get(position) +"");

            }
            else{
                getScore.setText(getString(R.string.blank));
            }


            getScore.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String[] words = playerText.getText().toString().split(" ");
                    if(getScore.hasFocus()) {
                        String getScoreString = getScore.getText().toString();

                        //Change Values
                        if (getScoreString.equals("")) {
                            playerScoreArray.set(Integer.parseInt(words[1])-1, -1);
                            currentGame.setTotalScore(playerScoreArray);
                        } else {
                            playerScoreArray.set(Integer.parseInt(words[1])-1, Integer.parseInt(getScoreString));
                            currentGame.setTotalScore(playerScoreArray);
                        }

                        //Update Application
                        if (currentGame.isValidScoresList()) {
                            updateTotalScore.setText(Integer.toString(currentGame.getTotalScore()));
                            currentGame.setListScore(playerScoreArray);
                            selectedDifficultyButton = currentGame.getDifficulty();
                            displayAchievementLevel();
                        } else {
                            updateTotalScore.setText(R.string.empty_string);
                            displayAchievementText.setText(R.string.empty_string);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            return itemView;
        }
    }
}
