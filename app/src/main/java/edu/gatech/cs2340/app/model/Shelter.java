package edu.gatech.cs2340.app.model;

import java.util.ArrayList;

/**
 * Represents everything important happening with Shelters.
 */
public class Shelter {
    private final int uniqueKey;
    private final String name;
    private final ArrayList<Integer> capacity;
    private double[] longitudeLatitude = new double[2];
    private final String address;
    private final String specialNotes;
    private final String phoneNumber;
    private String capacityString;
    private String longLatString;
    private boolean[] allowed = new boolean[10]; // [men, women, nonBinary, families, famChildren,
    // famNewborn, children, youngAdults, veterans, anyone]
    private int totalCapacity = 0;
    private int remainingCapacity;
    private String searchRestrictions = "Unspecified";
    private String shelterInfoString;

    private Shelter(int uniqueKey, String name, ArrayList<Integer> capacity, String restrictions,
                    double[] longitudeLatitude, String address, String specialNotes,
                    String phoneNumber) {
        this.uniqueKey = uniqueKey;
        this.name = name;
        this.capacity = capacity;
        this.longitudeLatitude = longitudeLatitude;
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
        makeLongLat(longitudeLatitude);
        makeSearchRestrictionsString(restrictions);
        for (int i = 0; i < capacity.size(); i++) {
            totalCapacity += capacity.get(i);
        }
        remainingCapacity = totalCapacity;
        shelterInfoString = "Capacity: " + capacityString +"\n\nRemaining beds: "
                + remainingCapacity + "\n\n" + restrictions + "\n\n" + longLatString + "\n\n"
                + address + "\n\n" + phoneNumber + "\n\nNote: " + specialNotes;
    }

    private void makeLongLat(double[] longitudeLatitude) {
        if (longitudeLatitude[0] < 0) {
            longLatString = "" + longitudeLatitude[0] * -1 + "° W";
        } else {
            longLatString = "" + longitudeLatitude[0] + "° E";
        }
        if (longitudeLatitude[1] < 0) {
            longLatString += ", " + longitudeLatitude[1] * -1 + "° S";
        } else {
            longLatString += ", " + longitudeLatitude[1] + "° N";
        }
    }

    public Shelter(int uniqueKey, String name, ArrayList<Integer> capacity, int remainingCapacity,
                   String restrictions, double[] longitudeLatitude, String address,
                   String specialNotes, String phoneNumber) {

        this(uniqueKey, name, capacity, restrictions, longitudeLatitude, address, specialNotes,
                phoneNumber);
        this.remainingCapacity = remainingCapacity;
    }
    @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
    private void makeSearchRestrictionsString(String restrictions) {
        String lcRestrictions = restrictions.toLowerCase();
        if (lcRestrictions.contains("women")) {
            allowed[1] = true;
            searchRestrictions = addToString("Women", searchRestrictions);
        }
        else if (lcRestrictions.contains("men")) {
            allowed[0] = true;
            searchRestrictions = addToString("Men", searchRestrictions);
        }
        if (lcRestrictions.contains("non-binary")
                || lcRestrictions.contains("nonbinary")) {
            allowed[2] = true;
            searchRestrictions = addToString("Non-binary", searchRestrictions);
        }
        if (lcRestrictions.contains("families w/ children")
                || lcRestrictions.contains("families with children")) {
            allowed[4] = true;
            searchRestrictions = addToString("Families with children", searchRestrictions);
        }
        if (lcRestrictions.contains("newborn")) {
            allowed[5] = true;
            searchRestrictions = addToString("Families with newborns", searchRestrictions);
        }
        if (lcRestrictions.contains("famil") && !allowed[4] && !allowed[5]) {
            allowed[3] = true;
            searchRestrictions = addToString("Families", searchRestrictions);
        }
        if (lcRestrictions.contains("children") && !allowed[4]) {
            allowed[6] = true;
            searchRestrictions = addToString("Children", searchRestrictions);
        }
        if (lcRestrictions.contains("young adult")) {
            allowed[7] = true;
            searchRestrictions = addToString("Young adult", searchRestrictions);
        }
        if (lcRestrictions.contains("veteran")) {
            allowed[8] = true;
            searchRestrictions = addToString("Veteran", searchRestrictions);
        }
        if (lcRestrictions.contains("anyone")) {
            allowed[9] = true;
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
    public String getCapacityString() { return capacityString; }
    public ArrayList<Integer> getCapacity() { return capacity; }
    public double[] getLongitudeLatitude() {return longitudeLatitude; }
    public String getNotes() { return specialNotes; }
    public String getLongLatString() { return longLatString; }
    public String getSearchRestrictions() { return searchRestrictions; }
    public String getShelterInfoString() { return shelterInfoString; }

    /**
     * [men, women, nonBinary, families, famChildren, famNewborn, children, youngAdults, veterans,
     * anyone]
     * @return The array of people allowed the shelter.
     */
    public boolean[] getAllowsArray() {
        return allowed;
    }
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




