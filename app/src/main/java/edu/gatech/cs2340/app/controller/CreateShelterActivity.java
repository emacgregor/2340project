package edu.gatech.cs2340.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;

import java.util.ArrayList;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.DataServiceFacade;
import edu.gatech.cs2340.app.model.Model;
import edu.gatech.cs2340.app.model.Restrictions;
import edu.gatech.cs2340.app.model.Shelter;
import edu.gatech.cs2340.app.model.ShelterDatabase;
import edu.gatech.cs2340.app.model.ShelterInfo;

public class CreateShelterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shelter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ((AppCompatActivity)this).getSupportActionBar().setTitle("Create a Shelter");

        final EditText nameText = (EditText) findViewById(R.id.shelterName);
        final EditText addressText = (EditText) findViewById(R.id.address);
        final EditText descriptionText = (EditText) findViewById(R.id.description);
        final EditText phoneText = (EditText) findViewById(R.id.phone);
        final EditText capacityText = (EditText) findViewById(R.id.capacityInput);
        final EditText longitudeText = (EditText) findViewById(R.id.longitudeInput);
        final EditText latitudeText = (EditText) findViewById(R.id.latitudeInput);
        longitudeText.setText("" + Model.getLongLat()[1]);
        latitudeText.setText("" + Model.getLongLat()[0]);
        final Checkable menChk = (CheckBox) findViewById(R.id.menBox);
        final Checkable womenChk = (CheckBox) findViewById(R.id.womenBox);
        final Checkable nonBChk = (CheckBox) findViewById(R.id.nonbinaryBox);
        final Checkable famChk = (CheckBox) findViewById(R.id.familyBox);
        final Checkable famChildChk = (CheckBox) findViewById(R.id.famChildBox);
        final Checkable famNewChk = (CheckBox) findViewById(R.id.famNewbornBox);
        final Checkable childChk = (CheckBox) findViewById(R.id.childrenBox);
        final Checkable youngAdultChk = (CheckBox) findViewById(R.id.youngBox);
        final Checkable veteranChk = (CheckBox) findViewById(R.id.veteranBox);
        final Checkable anyoneChk = (CheckBox) findViewById(R.id.anyoneBox);
        Button launchBtn = findViewById(R.id.confirm_button);
        launchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String failureString = "";
                if (capacityText.getText().toString().length() < 1) {
                    failureString += "Capacity cannot be empty. ";
                }
                if (longitudeText.getText().toString().length() < 1
                        || latitudeText.getText().toString().length() < 1) {
                    failureString += "Latitude or longitude cannot be empty. ";
                }
                if (failureString.length() < 1) {
                    ArrayList<Integer> capacity = new ArrayList<>();
                    capacity.add(Integer.parseInt(capacityText.getText().toString()));
                    Restrictions restrictions = new Restrictions(new boolean[]{menChk.isChecked(),
                            womenChk.isChecked(), nonBChk.isChecked(), famChk.isChecked(),
                            famChildChk.isChecked(), famNewChk.isChecked(), childChk.isChecked(),
                            youngAdultChk.isChecked(), veteranChk.isChecked(), anyoneChk.isChecked()});
                    int uniqueKey = ShelterDatabase.getNewUniqueKey();
                    ShelterInfo shelterInfo = new ShelterInfo(nameText.getText().toString(),
                            addressText.getText().toString(), descriptionText.getText().toString(),
                            phoneText.getText().toString(), "");
                    double[] longLat = new double[]{
                            Double.parseDouble(latitudeText.getText().toString()),
                            Double.parseDouble(longitudeText.getText().toString())};

                    Shelter newShelter = new Shelter(uniqueKey, capacity, longLat, shelterInfo, restrictions);
                    ShelterDatabase.addShelter(newShelter);
                    DataServiceFacade.getData().add(newShelter.makeDataElement());

                    Intent mapScreen = new Intent(CreateShelterActivity.this, MapActivity.class);
                    startActivity(mapScreen);
                    finish();
                } else {
                    Snackbar.make(view, failureString, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

}
