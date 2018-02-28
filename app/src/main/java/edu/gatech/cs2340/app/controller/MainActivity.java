package edu.gatech.cs2340.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.ShelterListActivity;
import edu.gatech.cs2340.app.model.Model;
import edu.gatech.cs2340.app.model.Shelter;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "CSV_Reader";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button logOutBtn = (Button) findViewById(R.id.button);
        Button sheltersBtn = (Button) findViewById(R.id.button2);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent welcomeScreen = new Intent(MainActivity.this, WelcomeScreen.class);
                startActivity(welcomeScreen);
            }
        });


        sheltersBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                readSDFile();
                Intent shelterScreen = new Intent(MainActivity.this, ShelterListActivity.class);
                startActivity(shelterScreen);

            }
        });

    }

    private void readSDFile() {
        Model model = Model.getInstance();

        try {
            //Open a stream on the raw file
            InputStream is = getResources().openRawResource(R.raw.homelessdb);
            //From here we probably should call a model method and pass the InputStream
            //Wrap it in a BufferedReader so that we get the readLine() method
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            String line;
            br.readLine(); //get rid of header line
            while ((line = br.readLine()) != null) {
                Log.d(MainActivity.TAG, line);
                String[] tokens = line.split(",");
                int uniqueKey = Integer.parseInt(tokens[0]);
                String shelterName = tokens[1];
                int capacity = -1;
                if (!tokens[2].equals("")) {
                    capacity = Integer.parseInt(tokens[2]);
                }
                String restrictions = tokens[3];
                Double latitutude = Double.parseDouble(tokens[4]);
                Double longitude = Double.parseDouble(tokens[5]);
                String address = tokens[6];
                String specialNotes = tokens[7];
                String phoneNumber = tokens[8];


                model.addShelter(new Shelter(uniqueKey, shelterName, capacity,
                        restrictions, latitutude, longitude, address, specialNotes, phoneNumber));
            }
            br.close();
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "error reading assets", e);
        }
    }
}


    


