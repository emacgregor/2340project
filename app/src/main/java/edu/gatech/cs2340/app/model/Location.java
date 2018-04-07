package edu.gatech.cs2340.app.model;

/**
 * Holds information about longitude and latitude.
 */

public class Location {
    private final double longitude;
    private final double latitude;
    private String longLatString;

    /**
     * Constructor for class Location
     * @param latitude The latitude of the place
     * @param longitude The longitude of the place
     */
    public Location(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        makeLongLat();
    }

    /**
     * Returns the longitude and latitude in string form.
     * @return A String of longitude and latitude.
     */
    public String toString() {
        return longLatString;
    }
    private void makeLongLat() {
        if (longitude < 0) {
            longLatString = "" + longitude * -1 + "° W";
        } else {
            longLatString = "" + longitude + "° E";
        }
        if (latitude < 0) {
            longLatString += ", " + latitude * -1 + "° S";
        } else {
            longLatString += ", " + latitude + "° N";
        }
    }

    /**
     * Returns the location's longitude
     * @return The longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Returns the location's latitude.
     * @return The latitude.
     */
    public double getLatitude() {
        return latitude;
    }
}
