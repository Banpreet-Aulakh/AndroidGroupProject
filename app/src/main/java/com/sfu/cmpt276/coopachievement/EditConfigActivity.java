package com.sfu.cmpt276.coopachievement;

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
import com.sfu.cmpt276.coopachievement.model.GameHistory;
import com.sfu.cmpt276.coopachievement.model.Singleton;

import java.util.ArrayList;

/*
 * EditConfig Activity is responsible for creating new config and editing an already created
 * config. Data includes the name of a game, input data of great and poor score, number of player
 * as well as printing dynamically all the achievement threshold
 */

public class EditConfigActivity extends AppCompatActivity {
    private final static String positionCodeName = "POSITION_ACTIVITY";
    static private int EASY = 0;
    static private int MEDIUM = 1;
    static private int HARD = 2;
    private Singleton gameConfigList = Singleton.getInstance();
    private GameConfig game;


    private int [] txtThresholdAchievementID = {
            R.id.config_lowly_leech_val,
            R.id.config_horrendous_val,
            R.id.config_bogus_val,
            R.id.config_terrible_val,
            R.id.config_goofy_val,
            R.id.config_dastardly_val,
            R.id.config_awesome_val,
            R.id.config_epic_val,
            R.id.config_fabulous_val
    };

    //getting the address of achievement text view and change text based on theme index
    private int[] txtThresholdAchievement={
            R.id.config_lowly_leech,
            R.id.config_horrendous_hagfish,
            R.id.config_bogus_blowfish,
            R.id.config_terrible_trolls,
            R.id.config_goofy_goblins,
            R.id.config_dastardly_dragons,
            R.id.config_awesome_alligators,
            R.id.config_epic_elephants,
            R.id.config_fabulous_fairies
    };
    //Index of config array, if position = -1, you are creating a new config
    private int position;
    private boolean isCreateConfig;

    private String gameName;

    private String poorScoreTxt;
    private int poorScore;

    private String greatScoreTxt;
    private int greatScore;

    private EditText gameEditTxt;
    private EditText poorEditTxt;
    private EditText greatEditTxt;
    private EditText numPlayers;

    private int selectedDifficultyButton;


    private TextView achievementViews;

    public static Intent getIntent(Context context, int position){
        Intent intent = new Intent(context, EditConfigActivity.class);
        intent.putExtra(positionCodeName, position);
        return intent;
    }

    private void getDataFromIntent(){
        Intent intent = getIntent();

        position = intent.getIntExtra(positionCodeName, -1);
        if(position != -1){
            isCreateConfig = false;
        }
        else{
            isCreateConfig = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_delete_configuration);

        getDataFromIntent();
        ActionBar toolbar = getSupportActionBar();

        gameEditTxt = (EditText) findViewById(R.id.editTextGameName);
        poorEditTxt = (EditText) findViewById(R.id.editTextPoorScore);
        greatEditTxt = (EditText) findViewById(R.id.editTextGreatScore);
        numPlayers = findViewById(R.id.editText_numPlayers);
        numPlayers.addTextChangedListener(checkFinished);
        gameEditTxt.addTextChangedListener(checkFinished);
        poorEditTxt.addTextChangedListener(checkFinished);
        greatEditTxt.addTextChangedListener(checkFinished);



        if(isCreateConfig){
            toolbar.setTitle(R.string.create_config_title);
            game = new GameConfig();
        }
        else{
            toolbar.setTitle(R.string.edit_config_title);
            setEditConfigValues();

        }
        setupDifficultyRadioButtons(game);
        selectedDifficultyButton = MEDIUM;
        toolbar.setDisplayHomeAsUpEnabled(true);
        int selectedTheme =gameConfigList.getThemeIndex();

        checkThemeAndPopulateThreshold(selectedTheme);

    }

    private void checkThemeAndPopulateThreshold(int selectedTheme) {
        if(selectedTheme == 0){
            String[] themeArray=getResources().getStringArray(R.array.mythical);
            for(int i=0;i<txtThresholdAchievement.length;i++)
            {
                String temp = themeArray[i];
                TextView tv = (TextView) findViewById(txtThresholdAchievement[i]);
                tv.setText(temp);
            }

        }
        if (selectedTheme==1)
        {
            String[] themeArray=getResources().getStringArray(R.array.paw_patrol);
            for(int i=0;i<txtThresholdAchievement.length;i++)
            {
                String temp = themeArray[i];
                TextView tv = (TextView) findViewById(txtThresholdAchievement[i]);
                tv.setText(temp);
            }

        }
        if(selectedTheme==2)
        {
            String[] themeArray=getResources().getStringArray(R.array.dinosaur);
            for(int i=0;i<txtThresholdAchievement.length;i++)
            {
                String temp = themeArray[i];
                TextView tv = (TextView) findViewById(txtThresholdAchievement[i]);
                tv.setText(temp);
            }

        }


    }

    //Get Values from a game config to Edit
    private void setEditConfigValues(){

        game = gameConfigList.getGameConfigList().get(position);

        gameName = game.getGameName();
        greatScore = game.getGreatScore();
        poorScore = game.getPoorScore();

        gameEditTxt.setText(gameName);
        poorEditTxt.setText(Integer.toString(poorScore));
        greatEditTxt.setText(Integer.toString(greatScore));

    }

    //ToolBar for Save and Delete Button
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(!isCreateConfig){
            getMenuInflater().inflate(R.menu.menu_config_edit, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.menu_config_create, menu);
        }

        return true;
    }
    //Save / Delete Game
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Get Input
        gameEditTxt = (EditText) findViewById(R.id.editTextGameName);
        poorEditTxt = (EditText) findViewById(R.id.editTextPoorScore);
        greatEditTxt = (EditText) findViewById(R.id.editTextGreatScore);

        //Convert to String
        gameName = gameEditTxt.getText().toString();
        poorScoreTxt = poorEditTxt.getText().toString();
        greatScoreTxt = greatEditTxt.getText().toString();

        switch(item.getItemId()){

            case R.id.saveConfig:

                //Save item. Check Items are valid
                if(!gameName.equals(getString(R.string.blank)) && !poorScoreTxt.equals(getString(R.string.blank))
                        && !greatScoreTxt.equals(getString(R.string.blank))){
                    Toast.makeText(EditConfigActivity.this, getString(R.string.save_config),
                            Toast.LENGTH_LONG).show();

                    //Convert String to int

                    greatScore = Integer.parseInt(greatScoreTxt);
                    poorScore = Integer.parseInt(poorScoreTxt);


                    //Final Check

                    if(greatScore > poorScore && greatScore-poorScore > 8) {

                        //Put gameName, greatScore, and poorScore into singleton here.
                        game.setGameName(gameName);
                        game.setPoorScore(poorScore);
                        game.setGreatScore(greatScore);
                        if(isCreateConfig){
                            GameHistory historyInstance = new GameHistory(gameName);
                            game.setGameHistory(historyInstance);
                            gameConfigList.addConfig(game);
                            game.setAchievement_Thresholds(0);
                        }
                        else {
                            game.getAchievement_Thresholds().clear();
                            game.setAchievement_Thresholds(0);
                        }

                        ViewConfigListActivity.saveData(EditConfigActivity.this);
                        finish();

                    }
                    else{
                        Toast.makeText(EditConfigActivity.this, getString(R.string.great_score_config),
                                Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(EditConfigActivity.this, getString(R.string.missing_parameter_config),
                            Toast.LENGTH_LONG).show();
                }
                return true;


            case R.id.deleteConfig:

                //Remove index from singleton
                Toast.makeText(EditConfigActivity.this, getString(R.string.delete_config),
                        Toast.LENGTH_LONG).show();

                gameConfigList.removeConfig(position);
                ViewConfigListActivity.saveData(EditConfigActivity.this);
                Intent intent = new Intent(this, ViewConfigListActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                EditConfigActivity.this.finish();
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //function changes the achievement thresholds, called when radio button difficulty is changed or textfield changes
    private void populateAchievementThresholds(){
        gameEditTxt = (EditText) findViewById(R.id.editTextGameName);
        poorEditTxt = (EditText) findViewById(R.id.editTextPoorScore);
        greatEditTxt = (EditText) findViewById(R.id.editTextGreatScore);

        gameName = gameEditTxt.getText().toString();
        poorScoreTxt = poorEditTxt.getText().toString();
        greatScoreTxt = greatEditTxt.getText().toString();

        if(!gameEditTxt.getText().toString().isEmpty()
                && !greatEditTxt.getText().toString().isEmpty()
                && !poorEditTxt.getText().toString().isEmpty()
                && !numPlayers.getText().toString().isEmpty()) {

            greatScore = Integer.parseInt(greatScoreTxt);
            poorScore = Integer.parseInt(poorScoreTxt);
            if(greatScore > poorScore && greatScore-poorScore > 8) {
                //Put gameName, greatScore, and poorScore into singleton here.
                game.setGameName(gameName);
                game.setPoorScore(poorScore);
                game.setGreatScore(greatScore);
            }

            game.getAchievement_Thresholds().clear();
            game.setAchievement_Thresholds(selectedDifficultyButton);
            ArrayList<Integer> thresholdList = game.getAchievement_Thresholds();

            int numP = Integer.parseInt(numPlayers.getText().toString());

            achievementViews = findViewById(txtThresholdAchievementID[0]);
            achievementViews.setText(R.string.zero_points_string);
            for(int listCounter = 0; listCounter < 8; listCounter++){
                achievementViews = findViewById(txtThresholdAchievementID[listCounter + 1]);
                achievementViews.setText((thresholdList.get(listCounter)) * numP + getString(R.string.point_string));
            }
        }
    }

    private void setupDifficultyRadioButtons(GameConfig configuration) {
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
                    configuration.setAchievement_Thresholds(selectedDifficultyButton);
                    populateAchievementThresholds();
                }
            });
            group.addView(button);
        }
        RadioButton button = (RadioButton) group.getChildAt(MEDIUM);
        button.setChecked(true);
    }

    //Display Achievement Ranges when all values are put in
    private TextWatcher checkFinished = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
           populateAchievementThresholds();
        }
        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}