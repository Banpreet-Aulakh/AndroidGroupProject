package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OptionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static OptionActivity instance = null;
    private String selectedTheme;
    private TextView achievementStringList ;
    private Spinner themeSpinner;
    private ArrayAdapter<CharSequence> themeAdapter;
    private String [] theme1Array;
    private String [] theme2Array;
    private String [] theme3Array;

    private static final String GENERAL_PREFS_NAME = "AppPrefs";
    private static final String THEME_KEY = "Theme Choice";
    private String themeText;



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

        themeSpinner = findViewById(R.id.theme_spinner);
        themeAdapter= ArrayAdapter.createFromResource(this,R.array.theme_choice,
                android.R.layout.simple_spinner_item);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(themeAdapter);
        themeSpinner.setOnItemSelectedListener(this);


    }

    public static Intent makeIntent(Context context, String s) {
        Intent intent = new Intent(context, OptionActivity.class);
        //intent.putExtra(EXTRA_TITLE, s);
        return intent;
    }

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
                case "Animal":
                    theme2Array = getResources().getStringArray(R.array.animal);
                    StringBuilder builder2 = new StringBuilder();
                    for (String s:theme2Array){
                        builder2.append(s);
                        builder2.append("\n");
                    }
                    achievementStringList = findViewById(R.id.tvAchievementList);
                    achievementStringList.setText(builder2.toString());
                    break;
                case "Alien":
                    theme3Array = getResources().getStringArray(R.array.alien);
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

        Button saveButton= findViewById(R.id.saveOptionBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OptionActivity.this, "Saved : " + selectedTheme, Toast.LENGTH_SHORT).show();
                saveData(position,THEME_KEY);
                final MediaPlayer scan = MediaPlayer.create(OptionActivity.this,R.raw.scan);
                scan.start();
                finish();
            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void saveData(int position, String key ) {
        SharedPreferences prefs = this.getSharedPreferences(GENERAL_PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key,position);
        themeSpinner.setSelection(position);
        editor.apply();
    }

    static public int getData(Context context, String key, int id){
        SharedPreferences prefs = context.getSharedPreferences(GENERAL_PREFS_NAME,MODE_PRIVATE);
        context.getResources().getInteger(id);
        return prefs.getInt(key,0);
    }


}
