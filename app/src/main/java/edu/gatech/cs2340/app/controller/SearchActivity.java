package edu.gatech.cs2340.app.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.Shelter;
import edu.gatech.cs2340.app.model.ShelterAdapter;
import edu.gatech.cs2340.app.model.ShelterDatabase;

/**
 * This activity provides the back end of searching.
 */
@SuppressWarnings("CyclicClassDependency")
public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ShelterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayList<Shelter> shelterList = ShelterDatabase.getShelters();
        ListView searchList = findViewById(R.id.listview);

        ((AppCompatActivity)this).getSupportActionBar().setTitle("Shelter Search");

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                ShelterDatabase.setCurrentShelter(position);
                Intent intent = new Intent(v.getContext(), ShelterDetailActivity.class);
                intent.putExtra(ShelterDetailFragment.ARG_ITEM_ID, position);
                Context viewContext = v.getContext();
                viewContext.startActivity(intent);
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
        adapter.filter(newText);
        return false;
    }
}
