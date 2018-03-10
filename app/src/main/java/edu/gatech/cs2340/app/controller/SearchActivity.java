package edu.gatech.cs2340.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.Model;
import edu.gatech.cs2340.app.model.Shelter;
import edu.gatech.cs2340.app.model.ShelterAdapter;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ShelterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayList<Shelter> shelterList = Model.getInstance().getShelters();
        ListView searchList = findViewById(R.id.listview);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                Intent intent = new Intent(v.getContext(), ShelterDetailActivity.class);
                intent.putExtra(ShelterDetailFragment.ARG_ITEM_ID, position);
                v.getContext().startActivity(intent);
            }
        });

        adapter = new ShelterAdapter(this, shelterList);

        searchList.setAdapter(adapter);
        SearchView editSearch = findViewById(R.id.search);
        editSearch.setOnQueryTextListener(this);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filterByName(text);
        return false;
    }
}
