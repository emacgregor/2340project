package edu.gatech.cs2340.app.model;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Data element field heavily borrowed from Professor Waters's example.
 */

public class DataElement {
    private final String _name;
    private final String _description;
    private final Location _location;
    private final Restrictions _restrictions;

    /**
     * Create new element
     * @param name   the name of the element
     * @param desc   a textual description
     * @param location  the location of the element
     * @param restrictions Provides the restrictions for this data element.
     */
    public DataElement(String name, String desc, Location location, Restrictions restrictions) {
        _name = name;
        _description = desc;
        _location = location;
        _restrictions = restrictions;
    }

    @Override
    public String toString() {
        return  _name + "\n" + _description;
    }

    /*
     Getters for the data elements
     */

    /**
     * Returns data element name.
     * @return The name
     */
    private String getName() { return _name;}

    /**
     * Returns data element description
     * @return The description
     */
    private String getDescription() {  return _description; }

    /**
     * Returns data element latitude
     * @return The latitude
     */
    private double getLatitude() { return _location.getLatitude(); }

    /**
     * Returns data element longitude.
     * @return The longitude
     */
    private double getLongitude() { return _location.getLongitude(); }

    /**
     * Returns restrictions belonging to this data element.
     * @return The Restrictions
     */
    public Restrictions getRestrictions() {
        return _restrictions;
    }

    /**
     * Updates the map for this data element.
     * @param mMap The map to be updated
     */
    public void updateMap(GoogleMap mMap) {
        LatLng loc = new LatLng(getLatitude(), getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions = markerOptions.position(loc);
        markerOptions = markerOptions.title(getName());
        markerOptions = markerOptions.snippet(getDescription());
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }
}
