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

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.Model;
import edu.gatech.cs2340.app.model.Restrictions;

public class MapSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        final CheckBox menChk = (CheckBox) findViewById(R.id.checkBox3);
        final CheckBox womenChk = (CheckBox) findViewById(R.id.checkBox4);
        final CheckBox nonBChk = (CheckBox) findViewById(R.id.checkBox5);
        final CheckBox famChk = (CheckBox) findViewById(R.id.checkBox6);
        final CheckBox famChildChk = (CheckBox) findViewById(R.id.checkBox7);
        final CheckBox famNewChk = (CheckBox) findViewById(R.id.checkBox8);
        final CheckBox childChk = (CheckBox) findViewById(R.id.checkBox9);
        final CheckBox youngAdultChk = (CheckBox) findViewById(R.id.checkBox10);
        final CheckBox veteranChk = (CheckBox) findViewById(R.id.checkBox11);
        Button launchBtn = (Button) findViewById(R.id.button5);
        launchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restrictions mapRestrictions = new Restrictions(new boolean[]{menChk.isChecked(),
                        womenChk.isChecked(), nonBChk.isChecked(), famChk.isChecked(),
                        famChildChk.isChecked(), famNewChk.isChecked(), childChk.isChecked(),
                        youngAdultChk.isChecked(), veteranChk.isChecked()});
                Model model = Model.getInstance();
                model.setMapRestrictions(mapRestrictions);
                Intent mapScreen = new Intent(MapSearchActivity.this, MapActivity.class);
                startActivity(mapScreen);
            }
        });
    }

}
