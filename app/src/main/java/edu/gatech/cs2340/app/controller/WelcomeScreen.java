package edu.gatech.cs2340.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.Model;

/**
 * Controller for the welcome screen activity.
 */
public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button loginBtn = findViewById(R.id.log_in_btn);
        Button regBtn = findViewById(R.id.reg_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginScreen = new Intent(WelcomeScreen.this, LoginScreen.class);
                startActivity(loginScreen);
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registrationScreen = new Intent(WelcomeScreen.this,
                        RegistrationScreen.class);
                startActivity(registrationScreen);
            }
        });
        //Model.getInstance().readSDFile(getResources().openRawResource(R.raw.homelessdb));
        Model model = Model.getInstance();
        model.getSheltersFromDB();
    }

}
