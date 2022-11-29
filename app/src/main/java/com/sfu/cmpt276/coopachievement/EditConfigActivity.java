package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sfu.cmpt276.coopachievement.model.GameConfig;
import com.sfu.cmpt276.coopachievement.model.GameHistory;
import com.sfu.cmpt276.coopachievement.model.Singleton;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/*
 * EditConfig Activity is responsible for creating new config and editing an already created
 * config. Data includes the name of a game, input data of great and poor score, number of player
 * as well as printing dynamically all the achievement threshold
 */

public class EditConfigActivity extends AppCompatActivity {
    private final static String positionCodeName = "POSITION_ACTIVITY";
    public static final int CAMERA_ACTION_CODE = 100;
    private static final int DEFAULT_INDEX = -1;
    //static private int EASY = 0;
    static private int MEDIUM = 1;
    //static private int HARD = 2;
    private Singleton gameConfigList = Singleton.getInstance();
    private GameConfig game;
    private Bitmap boxPhoto;




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
    private ImageView boxImage;

    private int selectedDifficultyButton;


    private TextView achievementViews;

    public static Intent getIntent(Context context, int position){
        Intent intent = new Intent(context, EditConfigActivity.class);
        intent.putExtra(positionCodeName, position);
        return intent;
    }

    private void getDataFromIntent(){
        Intent intent = getIntent();

        position = intent.getIntExtra(positionCodeName, DEFAULT_INDEX);
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
        boxImage = findViewById(R.id.boxImagePreview);
        numPlayers = findViewById(R.id.editText_numPlayers);
        numPlayers.addTextChangedListener(checkFinished);
        gameEditTxt.addTextChangedListener(checkFinished);
        poorEditTxt.addTextChangedListener(checkFinished);
        greatEditTxt.addTextChangedListener(checkFinished);


        setupCameraButton();

        if(isCreateConfig){
            toolbar.setTitle(R.string.create_config_title);
            game = new GameConfig();
        }
        else{
            toolbar.setTitle(R.string.edit_config_title);
            setEditConfigValues();
            boxImage.setImageBitmap(game.getBoxImage());
        }
        setupDifficultyRadioButtons(game);
        selectedDifficultyButton = MEDIUM;
        toolbar.setDisplayHomeAsUpEnabled(true);

    }

    private void setupCameraButton(){
        Button btn = findViewById(R.id.addBoxPictureButton);
        if(ContextCompat.checkSelfPermission(EditConfigActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditConfigActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, CAMERA_ACTION_CODE);
        }
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_ACTION_CODE);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_ACTION_CODE && resultCode == RESULT_OK && data != null){
            Bundle bundle = data.getExtras();
            boxPhoto = (Bitmap) bundle.get("data");

            boxImage.setImageBitmap(boxPhoto);
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
                        //TODO: save image to config class
                        game.setBoxImage(boxPhoto);
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
    private void populateAchievementThreshold(){
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
            String lowlyLeechScore = getString(R.string.not_possible);
            if(thresholdList.get(0) != 0){
                lowlyLeechScore = getString(R.string.zero);
            }

            int selectedTheme =gameConfigList.getThemeIndex();


            //Change
            String[] achievementStringList = checkThemeAndPopulateThreshold(selectedTheme,numP,lowlyLeechScore,thresholdList);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    EditConfigActivity.this,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    achievementStringList
            );
            ListView achievementListView = findViewById(R.id.listViewAchievements);
            achievementListView.setAdapter(adapter);
        }
    }

    //Setup AchievementRanges
    private String[] checkThemeAndPopulateThreshold(int selectedTheme, int numP, String lowlyLeechScore, ArrayList<Integer>thresholdList) {

            String[] resourceArray = getResources().getStringArray(R.array.mythical);
        if(selectedTheme == 0){
            resourceArray = getResources().getStringArray(R.array.mythical);
        }
        if (selectedTheme==1) {
            resourceArray = getResources().getStringArray(R.array.paw_patrol);
        }
        if(selectedTheme==2) {
            resourceArray = getResources().getStringArray(R.array.dinosaur);
        }
        String[] themeArray={
                    resourceArray[0]+ getString(R.string.colon_space) + lowlyLeechScore,
                    resourceArray[1] + getString(R.string.colon_space) + numP * thresholdList.get(0),
                    resourceArray[2] + getString(R.string.colon_space) + numP * thresholdList.get(1),
                    resourceArray[3] + getString(R.string.colon_space) + numP * thresholdList.get(2),
                    resourceArray[4] + getString(R.string.colon_space) + numP * thresholdList.get(3),
                    resourceArray[5] + getString(R.string.colon_space) + numP * thresholdList.get(4),
                    resourceArray[6] + getString(R.string.colon_space) + numP * thresholdList.get(5),
                    resourceArray[7] + getString(R.string.colon_space)+ numP * thresholdList.get(6),
                    resourceArray[8] + getString(R.string.colon_space) + (numP * thresholdList.get(7))};
        return themeArray;
    }

    //Setup RadioButton Difficulty
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
                    populateAchievementThreshold();
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
           populateAchievementThreshold();
        }
        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}