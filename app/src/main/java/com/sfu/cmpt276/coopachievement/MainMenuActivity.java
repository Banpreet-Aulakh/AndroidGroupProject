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
    private static final int REQUEST_TAKE_PHOTO = 1;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;


    public Button captureBtn;
    public ImageView previewImage;
    public String currentPhotoPath;

    MaterialButton Button;
    ImageView imageView;
    Uri image_uri;



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
        Button = findViewById(R.id.capture_photo_button);
        imageView = findViewById(R.id.preview_image);

        captureBtn = findViewById(R.id.captureBtn);

        //storage permission request

//delete after done
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPermissions();
            }
        });


        setupConfigButton();
        setupOptionButton();

        //test youtube guide


        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA)==
                    PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED){
                        String[]permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,CAMERA_PERM_CODE);
                    }
                    else{
                        openCamera();
                    }

                }else{
                    openCamera();
                }
            }
        });

    }

    private void verifyPermissions() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){
            dispatchTakePictureIntent();
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    CAMERA_PERM_CODE);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode){
            case CAMERA_PERM_CODE:
                if(grantResults.length>0&&grantResults[0]==
                PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else{
                    Toast.makeText(this,"Permission needed!",Toast.LENGTH_SHORT).show();
                }
        }

    }


    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"new image");
        values.put(MediaStore.Images.Media.DESCRIPTION,"from the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(camIntent,CAMERA_REQUEST_CODE);


    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){
            imageView.setImageURI(image_uri);

        }

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

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                currentPhotoPath = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(MainMenuActivity.this,
                        "com.sfu.cmpt276.coopachievement.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }

        }
    }
}
