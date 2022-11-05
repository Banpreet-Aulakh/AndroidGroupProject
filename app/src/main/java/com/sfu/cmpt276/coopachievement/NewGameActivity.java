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
import com.sfu.cmpt276.coopachievement.model.GameHistory;
import com.sfu.cmpt276.coopachievement.model.GamePlayed;
import com.sfu.cmpt276.coopachievement.model.Singleton;

import java.util.List;

public class NewGameActivity extends AppCompatActivity {
    private Singleton configList;
    private GameConfig configuration;
    private GamePlayed currentGame;
    private GameHistory gamesPlayedList;
    private EditText numPlayers;
    private EditText totalScore;
    private TextView displayAchievementText;
    private int historyIndex;
    private int configIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configList = Singleton.getInstance();
        setContentView(R.layout.activity_new_game);
        Intent intent = getIntent();
        configIndex = intent.getIntExtra("configIndex", -1);
        historyIndex = intent.getIntExtra("historyIndex", -1);
        numPlayers = findViewById(R.id.numPlayersEditText);
        totalScore = findViewById(R.id.totalScoreEditText);

        numPlayers.addTextChangedListener(checkFinished);
        totalScore.addTextChangedListener(checkFinished);

        //configuration = configList.getGameConfigList().get(configIndex);

        ActionBar toolbar = getSupportActionBar();

        if(historyIndex != -1){
            //numPlayers.setText(//""+Game.getNumPlayers(), TextView.BufferType.EDITABLE);
            //totalScore.setText(//??+Game.getNumPlayers(), TextView.BufferType.EDITABLE);
            toolbar.setTitle("Edit Game");
            //this.currentGame = configList.getGameConfigList().get(configIndex).getGameHistory().getGame(historyIndex);
        }else{
            toolbar.setTitle("New Game: ");
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
                }else{
                    currentGame.setNumPlayers(getIntFromEditText(numPlayers));
                    currentGame.setTotalScore(getIntFromEditText(totalScore));
                    gamesPlayedList.addPlayedGame(currentGame);
                }
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
    private TextWatcher checkFinished = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String gameTotalScore = totalScore.getText().toString().trim();
            String gameNumPlayers = numPlayers.getText().toString().trim();

            if (!gameTotalScore.isEmpty() && !gameNumPlayers.isEmpty()) {

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}