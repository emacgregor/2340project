package edu.gatech.cs2340.app.model;

import android.util.Log;

import java.util.ArrayList;

public class Shelter {
    private final int uniqueKey;
    private final String name;
    private final ArrayList<Integer> capacity;
    private final String restrictions;
    private final Double longitude;
    private final Double latitude;
    private final String address;
    private final String specialNotes;
    private final String phoneNumber;
    private String capacityString;
    private String longLatString;
    private boolean men;
    private boolean women;
    private boolean nonBinary;
    private boolean families;
    private boolean famNewborn;
    private boolean famChildren;
    private boolean children;
    private boolean youngAdults;
    private boolean veterans;
    private boolean anyone;
    private int totalCapacity = 0;
    private int remainingCapacity;
    private String searchRestrictions = "Unspecified";

    private Shelter(int uniqueKey, String name, ArrayList<Integer> capacity, String restrictions,
                    Double longitude, Double latitude, String address, String specialNotes,
                    String phoneNumber) {
        this.uniqueKey = uniqueKey;
        this.name = name;
        this.capacity = capacity;
        this.restrictions = restrictions;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.specialNotes = specialNotes;
        this.phoneNumber = phoneNumber;
        capacityString = "";
        if (capacity.get(0) == -1) {
            capacityString += "Unspecified";
        } else {
            for (int i = 0; i < (capacity.size() - 1); i++) {
                capacityString = capacityString + capacity.get(i) + ", ";
            }
            capacityString += capacity.get(capacity.size() - 1);
        }
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
        makeSearchRestrictionsString();
        for (int i = 0; i < capacity.size(); i++) {
            totalCapacity += capacity.get(i);
        }
        remainingCapacity = totalCapacity;
    }

    public Shelter(int uniqueKey, String name, ArrayList<Integer> capacity, int remainingCapacity,
                   String restrictions, Double longitude, Double latitude, String address,
                   String specialNotes, String phoneNumber) {

        this(uniqueKey, name, capacity, restrictions, longitude, latitude, address, specialNotes,
                phoneNumber);
        this.remainingCapacity = remainingCapacity;
    }
    private void makeSearchRestrictionsString() {
        if (restrictions.toLowerCase().contains("women")) {
            women = true;
            if(!"Unspecified".equals(searchRestrictions)) {
                searchRestrictions += ", women";
            } else {
                searchRestrictions = "Women";
            }
        }
        else if (restrictions.toLowerCase().contains("men")) {
            men = true;
            if(!"Unspecified".equals(searchRestrictions)) {
                searchRestrictions += ", men";
            } else {
                searchRestrictions = "Men";
            }
        }
        if (restrictions.toLowerCase().contains("non-binary")
                || restrictions.toLowerCase().contains("nonbinary")) {
            nonBinary = true;
            if(!"Unspecified".equals(searchRestrictions)) {
                searchRestrictions += ", non-binary";
            } else {
                searchRestrictions = "Non-binary";
            }
        }
        if (restrictions.toLowerCase().contains("families w/ children")
                || restrictions.toLowerCase().contains("families with children")) {
            famChildren = true;
            if (!"Unspecified".equals(searchRestrictions)) {
                searchRestrictions += ", families with children";
            } else {
                searchRestrictions = "Families with children";
            }
        }
        if (restrictions.toLowerCase().contains("newborn")) {
            famNewborn = true;
            if (!"Unspecified".equals(searchRestrictions)) {
                searchRestrictions += ", families with newborns";
            } else {
                searchRestrictions = "Families with newborns";
            }
        }
        if (restrictions.toLowerCase().contains("famil") && !famChildren && !famNewborn) {
            families = true;
            if (!"Unspecified".equals(searchRestrictions)) {
                searchRestrictions += ", families";
            } else {
                searchRestrictions = "Families";
            }
        }
        if (restrictions.toLowerCase().contains("children") && !famChildren) {
            children = true;
            if(!"Unspecified".equals(searchRestrictions)) {
                searchRestrictions += ", children";
            } else {
                searchRestrictions = "Children";
            }
        }
        if (restrictions.toLowerCase().contains("young adult")) {
            youngAdults = true;
            if(!"Unspecified".equals(searchRestrictions)) {
                searchRestrictions += ", young adults";
            } else {
                searchRestrictions = "Young adults";
            }
        }
        if (restrictions.toLowerCase().contains("veteran")) {
            veterans = true;
            if(!"Unspecified".equals(searchRestrictions)) {
                searchRestrictions += ", veterans";
            } else {
                searchRestrictions = "Veterans";
            }
        }
        if (restrictions.toLowerCase().contains("anyone")) {
            anyone = true;
            if(!"Unspecified".equals(searchRestrictions)) {
                searchRestrictions += ", anyone";
            } else {
                searchRestrictions = "Anyone";
            }
        }
    }

    public String toString() {
        return name + " " + address;
    }
    public int getUniqueKey() {return uniqueKey; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRestrictions() { return restrictions; }
    public String getCapacityString() { return capacityString; }
    public ArrayList<Integer> getCapacity() { return capacity; }
    public Double getLatitude() {return latitude; }
    public Double getLongitude() {return longitude;}
    public String getNotes() { return specialNotes; }
    public String getLongLatString() { return longLatString; }
    public String getSearchRestrictions() { return searchRestrictions; }
    public boolean allowsMen() { return men; }
    public boolean allowsWomen() { return women; }
    public boolean allowsNonBinary() { return nonBinary; }
    public boolean allowsFamilies() { return families; }
    public boolean allowsFamiliesWithNewborns() { return famNewborn; }
    public boolean allowsFamiliesWithChildren() { return famChildren; }
    public boolean allowsChildren() { return children; }
    public boolean allowsYoungAdults() { return youngAdults; }
    public boolean allowsVeterans() { return veterans; }
    public boolean allowsAnyone() { return anyone; }
    public int getTotalCapacity() { return totalCapacity; }
    public int getRemainingCapacity() { return remainingCapacity; }

    public boolean canClaimBeds(int numBeds) {
        return !(numBeds > remainingCapacity);
    }
    public void claimBeds(int numBeds) {
        if (canClaimBeds(numBeds)) {
            return;
        } else {
            remainingCapacity -= numBeds;
        }
    }
    public boolean canReleaseBeds(int numBeds) {
        boolean canRelease = !((numBeds + remainingCapacity) > totalCapacity);
        return canRelease;
    }
    public void releaseBeds(int numBeds) {
        if (canReleaseBeds(numBeds)) {
            return;
        } else {
            remainingCapacity += numBeds;
        }
    }
}




