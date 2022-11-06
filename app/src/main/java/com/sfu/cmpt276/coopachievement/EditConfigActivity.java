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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sfu.cmpt276.coopachievement.model.GameConfig;
import com.sfu.cmpt276.coopachievement.model.GameHistory;
import com.sfu.cmpt276.coopachievement.model.Singleton;

import java.util.ArrayList;

public class EditConfigActivity extends AppCompatActivity {
    private final static String positionCodeName = "POSITION_ACTIVITY";

    private Singleton gameConfigList = Singleton.getInstance();
    private GameConfig game;

    //Index of Array, if position = -1, you are creating a new config
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
            toolbar.setTitle("Create Config");
            game = new GameConfig();
        }
        else{
            toolbar.setTitle("Edit Config");
            setEditConfigValues();

        }

        toolbar.setDisplayHomeAsUpEnabled(true);

    }
    private void setEditConfigValues(){
        game = gameConfigList.getGameConfigList().get(position);

        gameName = game.getGameName();
        greatScore = game.getGreatScore();
        poorScore = game.getPoorScore();

        gameEditTxt.setText(gameName);
        poorEditTxt.setText(Integer.toString(poorScore));
        greatEditTxt.setText(Integer.toString(greatScore));

    }

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


    //Singleton Class Here...
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Convert to String
        gameName = gameEditTxt.getText().toString();
        poorScoreTxt = poorEditTxt.getText().toString();
        greatScoreTxt = greatEditTxt.getText().toString();

        switch(item.getItemId()){

            case R.id.saveConfig:

                //Save item. Check Items are valid
                if(!gameName.equals("") && !poorScoreTxt.equals("") && !greatScoreTxt.equals("")){
                    Toast.makeText(EditConfigActivity.this, "Saving",
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
                            game.setAchievement_Thresholds();
                        }
                        else {
                            game.getAchievement_Thresholds().clear();
                            game.setAchievement_Thresholds();
                        }
                        finish();

                    }
                    else{
                        Toast.makeText(EditConfigActivity.this, "Great Score is less than Poor Score" +
                                " Or the range is less than 8",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(EditConfigActivity.this, "Missing Parameters",
                            Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.deleteConfig:

                //Remove index from singleton
                Toast.makeText(EditConfigActivity.this, "Deleting",
                        Toast.LENGTH_LONG).show();

                gameConfigList.removeConfig(position);
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

    private TextWatcher checkFinished = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
                game.setAchievement_Thresholds();
                ArrayList<Integer> thresholdList = game.getAchievement_Thresholds();

                int numP = Integer.parseInt(numPlayers.getText().toString());
                TextView lowlyView = findViewById(R.id.config_lowly_leech_val);
                lowlyView.setText(R.string.zero_points_string);
                TextView horrendousView = findViewById(R.id.config_horrendous_val);
                horrendousView.setText((thresholdList.get(0) + 1) * numP + getString(R.string.point_string));
                TextView bogusView = findViewById(R.id.config_bogus_val);
                bogusView.setText((thresholdList.get(1) + 1)* numP + getString(R.string.point_string));
                TextView terribleView = findViewById(R.id.config_terrible_val);
                terribleView.setText((thresholdList.get(2) + 1) * numP + 1 + getString(R.string.point_string));
                TextView goofyView = findViewById(R.id.config_goofy_val);
                goofyView.setText((thresholdList.get(3) + 1) * numP + 1 + getString(R.string.point_string));
                TextView dastardlyView = findViewById(R.id.config_dastardly_val);
                dastardlyView.setText((thresholdList.get(4) + 1) * numP+ getString(R.string.point_string));
                TextView awesomeView = findViewById(R.id.config_awesome_val);
                awesomeView.setText((thresholdList.get(5) + 1) * numP + getString(R.string.point_string));
                TextView epicView = findViewById(R.id.config_epic_val);
                epicView.setText((thresholdList.get(6) + 1) * numP + getString(R.string.point_string));
                TextView fabulousView = findViewById(R.id.config_fabulous_val);
                fabulousView.setText((thresholdList.get(7) + 1) * numP + getString(R.string.point_string));



            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}