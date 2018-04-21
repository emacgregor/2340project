package edu.gatech.cs2340.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.Model;

public class BanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button banBtn = findViewById(R.id.button19);
        Button unBBtn = findViewById(R.id.button20);
        Button backBtn = findViewById(R.id.button21);

        banBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainScreen = new Intent(BanActivity.this, MainActivity.class);
                startActivity(mainScreen);
            }
        });

        unBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainScreen = new Intent(BanActivity.this, MainActivity.class);
                startActivity(mainScreen);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainScreen = new Intent(BanActivity.this, MainActivity.class);
                startActivity(mainScreen);
            }
        });
    }

}
