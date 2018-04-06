package edu.gatech.cs2340.app.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.DataElement;
import edu.gatech.cs2340.app.model.DataServiceFacade;
import edu.gatech.cs2340.app.model.Location;
import edu.gatech.cs2340.app.model.Model;
import edu.gatech.cs2340.app.model.Restrictions;

/**
 * An activity that heavily borrows from Mr. Waters' example to implement map related things.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener{

    /** holds the map object returned from Google */
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //save the map instance returned from Google
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);

        //reference to our GRASP Controller interface to the model
        final DataServiceFacade dataService = DataServiceFacade.getInstance();

        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                //add a new item where the touch happened, for non-hardcoded data, we would need
                //to launch an activity with a form to enter the data.
                dataService.addDataElement("This is still in development", "lol",
                        new Location(latLng.latitude, latLng.longitude),
                        new Restrictions(new boolean[9]));
                Log.d("new pin", latLng.latitude + " " + latLng.longitude);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(dataService.getLastElementAdded().getName());
                markerOptions.snippet(dataService.getLastElementAdded().getDescription());

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            }
        });

        //get the data to display
        List<DataElement> dataList = dataService.getData();

        //iterate through the list and add a pin for each element in the model
        for (DataElement de : dataList) {
            Model model = Model.getInstance();
            Restrictions mapRestrictions = model.getMapRestrictions();
            if (mapRestrictions.hasMatch(de.getRestrictions())) {
                LatLng loc = new LatLng(de.getLatitude(), de.getLongitude());
                mMap.addMarker(new MarkerOptions().position(loc).title(de.getName()).snippet(
                        de.getDescription()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                Log.d(de.getName(), de.getLatitude() + " " + de.getLongitude());
            } else {
                Log.d("No match for " + mapRestrictions + de.getRestrictions(), "lol");
            }
        }

        //Use a custom layout for the pin data
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        Model model = Model.getInstance();
        int uniqueKey = model.findIdByName(marker.getTitle());
        if (uniqueKey == -1) {
            return;
        }
        Context context = this.getApplicationContext();
        Intent intent = new Intent(context, ShelterDetailActivity.class);
        intent.putExtra(ShelterDetailFragment.ARG_ITEM_ID,
                uniqueKey);
        Log.d("I am here", "lol");

        Model.getInstance().setCurrentShelter(model.findItemById(uniqueKey));
        context.startActivity(intent);
    }

    /**
     * This class implements a custom layout for the pin
     */
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        /**
         * Make the adapter
         */
        @SuppressLint("InflateParams")
        CustomInfoWindowAdapter(){
            // hook up the custom layout view in res/custom_map_pin_layout.xml
            myContentsView = getLayoutInflater().inflate(R.layout.custom_map_pin_layout, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = myContentsView.findViewById(R.id.title);
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = myContentsView.findViewById(R.id.snippet);
            tvSnippet.setText(marker.getSnippet());

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

    }
}

