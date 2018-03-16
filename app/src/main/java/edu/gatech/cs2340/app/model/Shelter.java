package edu.gatech.cs2340.app.model;

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
            StringBuilder sb = new StringBuilder(capacityString);
            for (int i = 0; i < (capacity.size() - 1); i++) {
                sb.append(new StringBuilder(capacity.get(i)));
                sb.append(", ");
            }
            sb.append(capacity.get(capacity.size() - 1));
            capacityString = sb.toString();
        }
        makeLongLat(longitude, latitude);
        makeSearchRestrictionsString();
        for (int i = 0; i < capacity.size(); i++) {
            totalCapacity += capacity.get(i);
        }
        remainingCapacity = totalCapacity;
    }

    private void makeLongLat(double longitude, double latitude) {
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

    public Shelter(int uniqueKey, String name, ArrayList<Integer> capacity, int remainingCapacity,
                   String restrictions, Double longitude, Double latitude, String address,
                   String specialNotes, String phoneNumber) {

        this(uniqueKey, name, capacity, restrictions, longitude, latitude, address, specialNotes,
                phoneNumber);
        this.remainingCapacity = remainingCapacity;
    }
    @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
    private void makeSearchRestrictionsString() {
        String lcRestrictions = restrictions.toLowerCase();
        if (lcRestrictions.contains("women")) {
            women = true;
            searchRestrictions = addToString("Women", searchRestrictions);
        }
        else if (lcRestrictions.contains("men")) {
            men = true;
            searchRestrictions = addToString("Men", searchRestrictions);
        }
        if (lcRestrictions.contains("non-binary")
                || lcRestrictions.contains("nonbinary")) {
            nonBinary = true;
            searchRestrictions = addToString("Non-binary", searchRestrictions);
        }
        if (lcRestrictions.contains("families w/ children")
                || lcRestrictions.contains("families with children")) {
            famChildren = true;
            searchRestrictions = addToString("Families with children", searchRestrictions);
        }
        if (lcRestrictions.contains("newborn")) {
            famNewborn = true;
            searchRestrictions = addToString("Families with newborns", searchRestrictions);
        }
        if (lcRestrictions.contains("famil") && !famChildren && !famNewborn) {
            families = true;
            searchRestrictions = addToString("Families", searchRestrictions);
        }
        if (lcRestrictions.contains("children") && !famChildren) {
            children = true;
            searchRestrictions = addToString("Children", searchRestrictions);
        }
        if (lcRestrictions.contains("young adult")) {
            youngAdults = true;
            searchRestrictions = addToString("Young adult", searchRestrictions);
        }
        if (lcRestrictions.contains("veteran")) {
            veterans = true;
            searchRestrictions = addToString("Veteran", searchRestrictions);
        }
        if (lcRestrictions.contains("anyone")) {
            anyone = true;
            searchRestrictions = addToString("Anyone", searchRestrictions);
        }
    }
    private String addToString(String people, String searchRestrictions) {
        if (!"Unspecified".equals(searchRestrictions)) {
            return searchRestrictions + ", " + people.toLowerCase();
        } else {
            return people;
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
            remainingCapacity -= numBeds;
        }
    }
    public boolean canReleaseBeds(int numBeds) {
        return !((numBeds + remainingCapacity) > totalCapacity);
    }
    public void releaseBeds(int numBeds) {
        if (canReleaseBeds(numBeds)) {
            remainingCapacity += numBeds;
        }
    }
}




