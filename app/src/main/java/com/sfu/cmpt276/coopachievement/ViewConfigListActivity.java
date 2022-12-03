package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


/*
 * The ViewConfigList Activity is serving as the first activity on the main menu
 * for the 1st iteration. Responsible for creating a new config
 * or printing the list of available configs,
 * which user can edit a config by clicking the corresponding list view item
 */

public class ViewConfigListActivity extends AppCompatActivity {

    private boolean isEditConfigMode;
    private static Singleton singleton = Singleton.getInstance();
    private static ArrayList<GameConfig> gameList = singleton.getGameConfigList();

    private ImageView emptyListImage;
    private ImageView arrowImage;
    private TextView emptyListTxt;
    private TextView helpCreateConfig;

    public static final String EXTRA_THEME = "Theme position in Option";


    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewConfigListActivity.class);
    }

    @Override
    protected void onResume(){
        super.onResume();
        getData(ViewConfigListActivity.this);

        emptyListImage = findViewById(R.id.imgemptyGameHistoryList);
        arrowImage = findViewById(R.id.imgArrow);
        emptyListTxt = findViewById(R.id.txtEmptyList);
        helpCreateConfig = findViewById(R.id.txtHelpCreateGameConfig);

        //default screen if no configs are available
        if(gameList.size() == 0){
            emptyListImage.setImageResource(R.drawable.alone);
            arrowImage.setImageResource(R.drawable.ic_baseline_arrow_forward_24);
            emptyListTxt.setText(R.string.empty_list_txt);
            helpCreateConfig.setText(R.string.help_create_txt);

        }
        else {
            emptyListImage.setImageResource(0);
            arrowImage.setImageResource(0);
            emptyListTxt.setText(getString(R.string.blank));
            helpCreateConfig.setText(getString(R.string.blank));
        }
        populateListView();
        registerListClick();
    }
// todo Need to make sure finish Affinity and back press does not break code

  @Override
    public void onBackPressed() {
     //Save instance code here
      super.onBackPressed();
      //finishAffinity();
      finishAndRemoveTask();
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_configured_list);

        Intent intent = getIntent();
        int theme_key = intent.getIntExtra(EXTRA_THEME,0);

        isEditConfigMode = true;
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Configured Games");

        createFloatingActionButton();

        //Set up for intent theme key
        //testing only


    }


    private void populateListView() {

        //Get Singleton Class
        /*ArrayAdapter<GameConfig> adapter = new ArrayAdapter<GameConfig>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                gameList
        );

        ListView list = (ListView) findViewById(R.id.ListEditConfiguredItems);
        list.setAdapter(adapter);*/

        ComplexAdapter complexlist = new ComplexAdapter(ViewConfigListActivity.this, R.layout.edit_config_row, gameList);
        ListView list = (ListView) findViewById(R.id.ListEditConfiguredItems);
        list.setAdapter(complexlist);
    }

    private void registerListClick(){
        ListView list = (ListView) findViewById(R.id.ListEditConfiguredItems);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //change activity code. Position extra is the "key" to the
                // ArrayList so we know which GameConfig to edit.
                Intent intent = GameHistoryActivity.getIntent(
                        ViewConfigListActivity.this, position);
                startActivity(intent);

            }
        });
    }
    private void createFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateConfig);
        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = EditConfigActivity.getIntent(
                            ViewConfigListActivity.this, -1);
                    startActivity(intent);
                }
            });

    }
    //Call Save Data After saving/editing a config or saving/editing a game played
    public static void saveData(Context context){
        SharedPreferences pref = context.getSharedPreferences("Object Preference", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gameList);
        edit.putString("SAVEDLIST", json);
        edit.apply();

    }
    public static void getData(Context context){
        SharedPreferences pref = context.getSharedPreferences("Object Preference", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = pref.getString("SAVEDLIST",null);

        Type type = new TypeToken<ArrayList<GameConfig>>() {}.getType();

        if(json != null){
        gameList = gson.fromJson(json, type);
        singleton.setNewGameConfigList(gameList);
        }
    }
    private class ComplexAdapter extends ArrayAdapter<GameConfig>{
        private int ResourceLayout;
        public ComplexAdapter(Context context, int resourceLayout, ArrayList<GameConfig> configList){
            super(context, resourceLayout, configList);
            this.ResourceLayout = resourceLayout;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            GameConfig gameConfig = gameList.get(position);
            String bitmapString;
            Bitmap boxPhoto;

            if(itemView == null){
                LayoutInflater inflater = LayoutInflater.from(ViewConfigListActivity.this);
                itemView = inflater.inflate(ResourceLayout, parent, false);
            }

            ImageView configImage = findViewById(R.id.gameConfigImage);
            TextView configName = findViewById(R.id.gameConfigName);

            configName.setText(gameConfig.getGameName());

            /*bitmapString = (gameConfig.getBoxImage());
            boxPhoto = EditConfigActivity.stringToBitmap(bitmapString);
            configImage.setImageBitmap(boxPhoto);*/

            return itemView;
        }
    }


}