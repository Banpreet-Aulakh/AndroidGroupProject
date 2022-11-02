package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = ViewConfigListActivity.getIntent(MainActivity.this, true);
        startActivity(intent);
    }
}