package edu.gatech.cs2340.app.model;

/**
 * Holds information about longitude and latitude.
 */

public class Location {
    double longitude;
    double latitude;
    String longLatString;
    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        makeLongLat();
    }
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
    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {
        return latitude;
    }
}
