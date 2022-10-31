package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EditConfiguredListActivity extends AppCompatActivity {
    @Override
    protected void onResume(){
        super.onResume();
        populateListView();
        registerListClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_configured_list);
        createFloatingActionButton();

    }


    private void populateListView() {

        //Get Singleton Class
        String[] placeholder = {"Blue", "Red", "Green", "Yellow", "Orange", "Black", "Yellow", "White", "Brown", "Pink", "Purple"};
        //
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                placeholder
        );

        ListView list = (ListView) findViewById(R.id.ListEditConfiguredItems);
        list.setAdapter(adapter);
    }

    private void registerListClick(){
        ListView list = (ListView) findViewById(R.id.ListEditConfiguredItems);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = CreateEditDeleteConfigurationActivity.getIntent(
                        EditConfiguredListActivity.this, position);
                startActivity(intent);

            }
        });
    }
    private void createFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateConfig);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CreateEditDeleteConfigurationActivity.getIntent(
                        EditConfiguredListActivity.this, -1);
                startActivity(intent);
            }
        });
    }

}