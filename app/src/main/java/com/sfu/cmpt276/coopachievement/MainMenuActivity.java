package com.sfu.cmpt276.coopachievement;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.hardware.Camera;
import static android.os.Environment.getExternalStoragePublicDirectory;

import com.google.android.material.button.MaterialButton;

/*
 * Main Menu is responsible for switching between ViewConfigList Activity and Option Activity
 */

public class MainMenuActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ActionBar toolbar = getSupportActionBar();
        assert toolbar != null;
        toolbar.setTitle("Main Menu");



        ImageView bannerImage = findViewById(R.id.main_banner);
        bannerImage.startAnimation(AnimationUtils.loadAnimation(
                getApplicationContext(),
                R.anim.fadein
        ));

        //previewImage = findViewById(R.id.preview_image);


        //storage permission request

//delete after done



        setupConfigButton();
        setupOptionButton();
        setupAboutButton();

        //test youtube guide




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
            Intent i = OptionActivity.makeIntent(MainMenuActivity.this, 0);
            startActivity(i);
        });

    }
    private void setupAboutButton() {
        Button btn = findViewById(R.id.aboutbtn);
        btn.setOnClickListener(v->{
            Intent i = new Intent(MainMenuActivity.this, AboutActivity.class);
            startActivity(i);
        });
    }




}
