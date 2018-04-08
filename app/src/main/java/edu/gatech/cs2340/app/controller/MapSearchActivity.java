package edu.gatech.cs2340.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.Model;
import edu.gatech.cs2340.app.model.Restrictions;

/**
 * This class handles everything to do with providing the restrictions for the map,
 * which shelters it will show.
 */
@SuppressWarnings("CyclicClassDependency")
public class MapSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Checkable menChk = (CheckBox) findViewById(R.id.checkBox3);
        final Checkable womenChk = (CheckBox) findViewById(R.id.checkBox4);
        final Checkable nonBChk = (CheckBox) findViewById(R.id.checkBox5);
        final Checkable famChk = (CheckBox) findViewById(R.id.checkBox6);
        final Checkable famChildChk = (CheckBox) findViewById(R.id.checkBox7);
        final Checkable famNewChk = (CheckBox) findViewById(R.id.checkBox8);
        final Checkable childChk = (CheckBox) findViewById(R.id.checkBox9);
        final Checkable youngAdultChk = (CheckBox) findViewById(R.id.checkBox10);
        final Checkable veteranChk = (CheckBox) findViewById(R.id.checkBox11);
        Button launchBtn = findViewById(R.id.button5);
        launchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restrictions mapRestrictions = new Restrictions(new boolean[]{menChk.isChecked(),
                        womenChk.isChecked(), nonBChk.isChecked(), famChk.isChecked(),
                        famChildChk.isChecked(), famNewChk.isChecked(), childChk.isChecked(),
                        youngAdultChk.isChecked(), veteranChk.isChecked()});
                Model.setMapRestrictions(mapRestrictions);
                Intent mapScreen = new Intent(MapSearchActivity.this, MapActivity.class);
                startActivity(mapScreen);
            }
        });
    }

}
