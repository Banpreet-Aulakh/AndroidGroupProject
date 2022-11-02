package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewConfigListActivity extends AppCompatActivity {
    private static final String ISEDITCONFIGMODECODE = "BOOLEAN EDITMODE";
    private boolean isEditConfigMode;


    //Comment out
    private List<String> placeholder = new ArrayList<>();


    public static Intent getIntent(Context context, Boolean isEdit){
        Intent intent = new Intent(context, ViewConfigListActivity.class);
        intent.putExtra(ISEDITCONFIGMODECODE, isEdit);
        return intent;
    }
    private void getDataFromIntent(){
        Intent intent = getIntent();
        isEditConfigMode = intent.getBooleanExtra(ISEDITCONFIGMODECODE, true);
    }


    @Override
    protected void onResume(){
        super.onResume();

        //Comment out
        placeholder.add("Hello");

        populateListView();
        registerListClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDataFromIntent();
        ActionBar toolbar = getSupportActionBar();
        if(isEditConfigMode){
            toolbar.setTitle("Edit Config List");
        }
        else{
            toolbar.setTitle("View Config List");
        }
        toolbar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_edit_configured_list);
        createFloatingActionButton();

    }


    private void populateListView() {

        //Get Singleton Class
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

                if(isEditConfigMode){
                Intent intent = EditConfigActivity.getIntent(
                        ViewConfigListActivity.this, position);
                startActivity(intent);}


                //List View Component
                else{


                }

            }
        });
    }
    private void createFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateConfig);
        if(!isEditConfigMode) {
            fab.setVisibility(View.GONE);
        }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = EditConfigActivity.getIntent(
                            ViewConfigListActivity.this, -1);
                    startActivity(intent);
                }
            });

    }
    public static void saveData(){

    }
    public static void getData(){

    }


}