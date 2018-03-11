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

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button loginBtn = findViewById(R.id.log_in_btn);
        Button regBtn = findViewById(R.id.reg_btn);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent loginScreen = new Intent(WelcomeScreen.this, LoginScreen.class);
                startActivity(loginScreen);
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent registrationScreen = new Intent(WelcomeScreen.this, RegistrationScreen.class);
                startActivity(registrationScreen);
            }
        });
        //Model.getInstance().readSDFile(getResources().openRawResource(R.raw.homelessdb));
        Model.getInstance().getSheltersFromDB();
    }

}
