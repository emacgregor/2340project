package edu.gatech.cs2340.app.model;

/**
 * Created by diana on 2/27/18.
 */

public class Shelter {
    private int uniqueKey;
    private String name;
    private int capacity;
    private String restrictions;
    private Double longitude;
    private Double latitude;
    private String address;
    private String specialNotes;
    private String phoneNumber;


    public Shelter(int uniqueKey, String name, int capacity, String restrictions, Double longitude,
                   Double latitude, String address, String specialNotes, String phoneNumber) {
        this.uniqueKey = uniqueKey;
        this.name = name;
        this.capacity = capacity;
        this.restrictions = restrictions;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.specialNotes = specialNotes;
        this.phoneNumber = phoneNumber;

    }

    public String toString() {
        return name + " " + address;
    }
    public int getUniqueKey() {return uniqueKey; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRestrictions() { return restrictions; }
    public int getCapacity() { return capacity; }
    public Double getLatitude() {return latitude; }
    public Double getLongitude() {return longitude;}
    public String getNotes() { return specialNotes; }
}




