package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/*
* Main Menu is responsible for switching between ViewConfigList Activity and Option Activity
*/

public class MainMenuActivity extends AppCompatActivity {
    //Singleton instance = Singleton.getInstance();
    //int themeIndex= instance.getThemeIndex();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Main Menu");

        ImageView bugImage = findViewById(R.id.main_banner);
        bugImage.startAnimation(AnimationUtils.loadAnimation(
                getApplicationContext(),
                R.anim.fadein
        ));



        setupConfigButton();
        setupOptionButton();

    }



    private void setupConfigButton() {
        Button btn = findViewById(R.id.configBtn);
        btn.setOnClickListener(v -> {
            Intent i = ViewConfigListActivity.makeIntent(MainMenuActivity.this);
            startActivity(i);
        });
    }

    private void setupOptionButton() {
        Button btn = findViewById(R.id.optionBtn);
        btn.setOnClickListener(v -> {
            Intent i = OptionActivity.makeIntent(MainMenuActivity.this,0);
            startActivity(i);
        });

    }
}