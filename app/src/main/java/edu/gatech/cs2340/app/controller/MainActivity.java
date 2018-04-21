package edu.gatech.cs2340.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.Model;
import edu.gatech.cs2340.app.model.ShelterDatabase;

/**
 * This is the main screen of the app.
 */
@SuppressWarnings("CyclicClassDependency")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button logOutBtn = findViewById(R.id.button);
        Button sheltersBtn = findViewById(R.id.button2);
        Button searchBtn = findViewById(R.id.button3);
        Button mapBtn = findViewById(R.id.button4);
        Button banBtn = findViewById(R.id.button6);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent welcomeScreen = new Intent(MainActivity.this, WelcomeScreen.class);
                startActivity(welcomeScreen);
                finish();
            }
        });

        sheltersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shelterScreen = new Intent(MainActivity.this, ShelterListActivity.class);
                ShelterDatabase.getSheltersFromDB();
                startActivity(shelterScreen);

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchScreen = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchScreen);
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapSearchScreen = new Intent(MainActivity.this, MapSearchActivity.class);
                startActivity(mapSearchScreen);
            }
        });

        banBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Model.currentUserAdmin()) {
                    Intent banScreen = new Intent(MainActivity.this, BanActivity.class);
                    startActivity(banScreen);
                }
            }
        });
    }
}


    


