package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sfu.cmpt276.coopachievement.model.GameConfig;
import com.sfu.cmpt276.coopachievement.model.Singleton;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Main Menu");

        setupConfigButton();
        setupOptionButton();

    }



    private void setupConfigButton() {
        Button btn = findViewById(R.id.configBtn);
        btn.setOnClickListener(v -> {
            int themeIndex=OptionActivity.getData(this,OptionActivity.getThemeKey(),OptionActivity.themeDefault);
            Intent i = ViewConfigListActivity.makeIntent(MainMenuActivity.this,themeIndex);
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