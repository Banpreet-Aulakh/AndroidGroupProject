package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sfu.cmpt276.coopachievement.model.Singleton;
/*
* Option Activity is responsible for letting user to choose theme and save it inside singleton class
* Also lists out the achievement name
*/
public class OptionActivity extends AppCompatActivity {
    private static OptionActivity instance = null;
    private String selectedTheme;
    private TextView achievementStringList ;
    private Spinner themeSpinner;
    private static int themeIndex;
    private ArrayAdapter themeAdapter;
    private String [] theme1Array;
    private String [] theme2Array;
    private String [] theme3Array;

    private static final String GENERAL_PREFS_NAME = "AppPrefs";
    private static final String THEME_KEY = "Theme Choice";

    private SharedPreferences prefs;
    SharedPreferences.Editor editor;



    public static OptionActivity getInstance() {
        if(instance == null) {
            instance = new OptionActivity();
        }
        return instance;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Option");
        toolbar.setDisplayHomeAsUpEnabled(true);
        Animation scaleUp,scaleDown;

        scaleUp=AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown=AnimationUtils.loadAnimation(this,R.anim.scale_down);


        //set up spinner and adapter
        themeSpinner = findViewById(R.id.theme_spinner);
        themeAdapter= ArrayAdapter.createFromResource(this,R.array.theme_choice,
                android.R.layout.simple_spinner_item);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(themeAdapter);

        //get shared preference to save the last position
        prefs=getSharedPreferences("LastSetting",Context.MODE_PRIVATE);
        editor=prefs.edit();
        final int lastClick = prefs.getInt("lastClick",0);
        themeSpinner.setSelection(lastClick);

        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTheme = themeSpinner.getSelectedItem().toString();

                int parentID=parent.getId();
                if(parentID ==R.id.theme_spinner){
                    switch (selectedTheme){
                        case "Mythical":
                            theme1Array = getResources().getStringArray(R.array.mythical);
                            StringBuilder builder1 = new StringBuilder();
                            for (String s:theme1Array){
                                builder1.append(s);
                                builder1.append("\n");
                            }
                            achievementStringList = findViewById(R.id.tvAchievementList);
                            achievementStringList.setText(builder1.toString());
                            break;
                        case "Paw Patrol":
                            theme2Array = getResources().getStringArray(R.array.paw_patrol);
                            StringBuilder builder2 = new StringBuilder();
                            for (String s:theme2Array){
                                builder2.append(s);
                                builder2.append("\n");
                            }
                            achievementStringList = findViewById(R.id.tvAchievementList);
                            achievementStringList.setText(builder2.toString());

                            break;
                        case "Dinosaur":
                            theme3Array = getResources().getStringArray(R.array.dinosaur);
                            StringBuilder builder3 = new StringBuilder();
                            for (String s:theme3Array){
                                builder3.append(s);
                                builder3.append("\n");
                            }
                            achievementStringList = findViewById(R.id.tvAchievementList);
                            achievementStringList.setText(builder3.toString());

                            break;

                        default:
                            break;
                    }
                }
                editor.putInt("lastClick",position).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button saveButton= findViewById(R.id.saveOptionBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OptionActivity.this, "Saved : " + selectedTheme, Toast.LENGTH_SHORT).show();
                final MediaPlayer saveSound = MediaPlayer.create(OptionActivity.this,R.raw.yeahboy1);
                saveSound.start();
                ImageView ball=findViewById(R.id.ball_image);
                ball.startAnimation(AnimationUtils.loadAnimation(
                        getApplicationContext(),
                        R.anim.rotate
                ));

                //save theme index inside singleton class
                themeIndex= themeSpinner.getSelectedItemPosition();
                Singleton instance = Singleton.getInstance();
                instance.setThemeIndex(themeIndex);

                saveButton.startAnimation(scaleUp);
                saveButton.startAnimation(scaleDown);
            }

        });


    }
    public static int getThemeIndex(){
        return themeIndex;
    }

    public static Intent makeIntent(Context context, int key) {
        Intent intent = new Intent(context, OptionActivity.class);
        intent.putExtra(getThemeKey(),getThemeIndex());
        return intent;
    }



    public void saveData(int position) {
        prefs = this.getSharedPreferences(GENERAL_PREFS_NAME,MODE_PRIVATE);
        editor = prefs.edit();
        editor.putInt("lastClick",position);
        editor.apply();
    }

    public static int getData(Context context, String key, int id){
        SharedPreferences prefs = context.getSharedPreferences(GENERAL_PREFS_NAME,MODE_PRIVATE);
        context.getResources().getInteger(id);
        return prefs.getInt("lastClick",0);
    }

    public static String getThemeKey(){
        return THEME_KEY;
    }


}
