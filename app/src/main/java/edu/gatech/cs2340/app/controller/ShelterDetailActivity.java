package edu.gatech.cs2340.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.Model;

/**
 * An activity representing a single Shelter detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ShelterListActivity}.
 */
@SuppressWarnings("CyclicClassDependency")
public class ShelterDetailActivity extends AppCompatActivity {

    @SuppressWarnings("FeatureEnvy")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            Intent intent = getIntent();
            arguments.putInt(ShelterDetailFragment.ARG_ITEM_ID,
                    intent.getIntExtra(ShelterDetailFragment.ARG_ITEM_ID, 1000));
            ShelterDetailFragment fragment = new ShelterDetailFragment();
            fragment.setArguments(arguments);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction = fragmentTransaction.add(R.id.shelter_detail_container, fragment);
            fragmentTransaction.commit();
            /*fragmentManager.beginTransaction()
                    .add(R.id.shelter_detail_container, fragment)
                    .commit();*/
        }

        final Spinner bedSpinner = findViewById(R.id.spinner3);
        Integer[] bedNums = new Integer[Model.getCurrentTotalCapacity()];
        for (int i = 0; i < Model.getCurrentTotalCapacity(); i++) {
            bedNums[i] = i + 1;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, bedNums);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bedSpinner.setAdapter(adapter);

        Button claimButton = findViewById(R.id.clmBtn);
        claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Model.claimBeds((int)bedSpinner.getSelectedItem())) {
                    Intent mainClass =  new Intent(ShelterDetailActivity.this,
                            MainActivity.class);
                    startActivity(mainClass);
                } else {
                    Snackbar waitBar = Snackbar.make(findViewById(R.id.shelter_detail_container),
                            getFailureString(),
                            Snackbar.LENGTH_SHORT);
                    waitBar.show();
                }
            }
        });
        Button releaseButton = findViewById(R.id.relBtn);
        releaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Model.releaseBeds((int)bedSpinner.getSelectedItem())) {
                    Intent mainClass =  new Intent(ShelterDetailActivity.this,
                            MainActivity.class);
                    startActivity(mainClass);
                } else {
                    Snackbar waitBar = Snackbar.make(findViewById(R.id.shelter_detail_container),
                            getFailureString(),
                            Snackbar.LENGTH_SHORT);
                    waitBar.show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ShelterListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private CharSequence getFailureString() {
        return Model.getFailureString();
    }
}
