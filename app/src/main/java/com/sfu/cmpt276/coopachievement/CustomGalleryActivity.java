package com.sfu.cmpt276.coopachievement;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.sfu.cmpt276.coopachievement.model.ViewPagerAdapter;

import java.io.File;
import java.util.ArrayList;

public class CustomGalleryActivity extends AppCompatActivity {

    ArrayList<String> f = new ArrayList<>();//list of file paths

    File[] listFile;

    private String folderName = "MyGamePhoto";

    //Create object of viewpager
    ViewPager myViewPager;
    ViewPagerAdapter myViewPagerAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        getFromSdcard();
        //initialize viewpager object and adapter
        myViewPager = findViewById(R.id.viewPagerMain);
        myViewPagerAdapter = new ViewPagerAdapter(this,f);
        myViewPager.setAdapter(myViewPagerAdapter);
    }

    public void getFromSdcard(){
        File file = new File(getExternalFilesDir(folderName),"/");
        if(file.isDirectory()){
            listFile = file.listFiles();
            for(int i = 0;i<listFile.length;i++){
                f.add(listFile[i].getAbsolutePath());
            }
        }
    }
}
