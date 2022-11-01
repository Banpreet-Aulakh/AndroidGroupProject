package com.sfu.cmpt276.coopachievement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class CreateEditDeleteConfigurationActivity extends AppCompatActivity {
    private final static String positionCodeName = "POSITION_ACTIVITY";
    private int position;
    private boolean isCreateConfig;

    public static Intent getIntent(Context context, int position){
        Intent intent = new Intent(context, CreateEditDeleteConfigurationActivity.class);
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

        if(isCreateConfig){
            toolbar.setTitle("Create Config");
        }
        else{
            toolbar.setTitle("Edit Config");
        }
        toolbar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(!isCreateConfig){
            getMenuInflater().inflate(R.menu.menu_config_edit,menu);
        }
        else{
            getMenuInflater().inflate(R.menu.menu_config_create, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.saveConfig:
                //Save item
                return true;

            case R.id.deleteConfig:
                //Remove index from singleton
                Toast.makeText(CreateEditDeleteConfigurationActivity.this, "Hello",
                        Toast.LENGTH_LONG).show();
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}