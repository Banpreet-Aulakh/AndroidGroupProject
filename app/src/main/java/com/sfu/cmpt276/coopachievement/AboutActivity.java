package com.sfu.cmpt276.coopachievement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {
    String[] urls;
    String[] urlNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("About Page");
        createList();
        createListClick();
    }
    protected void createList(){
        urls = getResources().getStringArray(R.array.links);
        urlNames = getResources().getStringArray(R.array.link_name);
        ListView resourceslist = findViewById(R.id.listViewResources);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                urlNames);
        resourceslist.setAdapter(adapter);

    }
    protected void createListClick(){
        ListView resourceslist = findViewById(R.id.listViewResources);
        resourceslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls[position]));
                startActivity(urlIntent);
            }
        });
    }
}