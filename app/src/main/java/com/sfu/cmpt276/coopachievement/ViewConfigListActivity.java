package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sfu.cmpt276.coopachievement.model.GameConfig;
import com.sfu.cmpt276.coopachievement.model.Singleton;

import java.util.List;

public class ViewConfigListActivity extends AppCompatActivity {

    private boolean isEditConfigMode;

    private List<GameConfig> gameList = Singleton.getInstance().getGameConfigList();

    private ImageView emptyListImage;
    private ImageView arrowImage;
    private TextView emptyListTxt;
    private TextView helpCreateConfig;

    @Override
    protected void onResume(){
        super.onResume();

        emptyListImage = findViewById(R.id.imgemptyConfigList);
        arrowImage = findViewById(R.id.imgArrow);
        emptyListTxt = findViewById(R.id.txtEmptyList);
        helpCreateConfig = findViewById(R.id.txtHelpCreate);

        // :)

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
            populateListView();
            registerListClick();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_configured_list);

        isEditConfigMode = true;
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Configured Games");

        createFloatingActionButton();

    }


    private void populateListView() {

        //Get Singleton Class
        ArrayAdapter<GameConfig> adapter = new ArrayAdapter<GameConfig>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                gameList
        );

        ListView list = (ListView) findViewById(R.id.ListEditConfiguredItems);
        list.setAdapter(adapter);
    }

    private void registerListClick(){
        ListView list = (ListView) findViewById(R.id.ListEditConfiguredItems);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //change activity code. I think the position extra can be the "key" to the
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

    }
    public static void getData(Context context){

    }


}