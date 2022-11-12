package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class OptionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static OptionActivity instance = null;
    private String selectedAchievement,selectedTheme;
    private TextView tvAchievement,tvTheme;
    private Spinner achievementSpinner,themeSpinner;
    private ArrayAdapter<CharSequence> achievementAdapter,themeAdapter;

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

        achievementSpinner = findViewById(R.id.achievement_spinner);
        achievementAdapter = ArrayAdapter.createFromResource(this,R.array.achievement_levels,
                android.R.layout.simple_spinner_item);
        achievementAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        achievementSpinner.setAdapter(achievementAdapter);
        achievementSpinner.setOnItemSelectedListener(this);

    }

    public static Intent makeIntent(Context context, String s) {
        Intent intent = new Intent(context, OptionActivity.class);
        //intent.putExtra(EXTRA_TITLE, s);

        return intent;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        themeSpinner = findViewById(R.id.theme_spinner);

        selectedAchievement = achievementSpinner.getSelectedItem().toString();

        int parentID=parent.getId();
        if(parentID ==R.id.achievement_spinner){
            switch (selectedAchievement){
                case"Select Achievement":
                    themeAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                    R.array.default_theme_array,android.R.layout.simple_spinner_dropdown_item);
                    break;
                case "Lowly Leech":
                    themeAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.lowly_leech_themes_array,android.R.layout.simple_spinner_dropdown_item);
                    break;
                case "Horrendous Hagfish":
                    themeAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.horrendous_hagfish_themes_array,android.R.layout.simple_spinner_dropdown_item);
                    break;
                case "Bogus Blowfish":
                    themeAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.bogus_blowfish_themes_array,android.R.layout.simple_spinner_dropdown_item);
                    break;
                case "Terrible Trolls":
                    themeAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.terrible_trolls_themes_array,android.R.layout.simple_spinner_dropdown_item);
                    break;
                case "Goofy Goblins":
                    themeAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.goofy_goblins_themes_array,android.R.layout.simple_spinner_dropdown_item);
                    break;
                case "Dastardly Dragons":
                    themeAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.dastardly_dragons_themes_array,android.R.layout.simple_spinner_dropdown_item);
                    break;
                case "Awesome Alligators":
                    themeAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.awesome_alligators_themes_array,android.R.layout.simple_spinner_dropdown_item);
                    break;
                case "Epic Elephants":
                    themeAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.epic_elephants_themes_array,android.R.layout.simple_spinner_dropdown_item);
                    break;
                case "Fabulous Fairies":
                    themeAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.fabulous_fairies_themes_array,android.R.layout.simple_spinner_dropdown_item);
                    break;

                default:
                    break;
            }
            themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            themeSpinner.setAdapter(themeAdapter);

            themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedTheme = themeSpinner.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Button saveButton= findViewById(R.id.saveOptionBtn);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OptionActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    finishAndRemoveTask();
                }
            });
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}