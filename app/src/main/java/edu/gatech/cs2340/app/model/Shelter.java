package edu.gatech.cs2340.app.model;

import java.util.ArrayList;

/**
 * Represents everything important happening with Shelters.
 */
public class Shelter {
    private final int uniqueKey;
    private final String name;
    private final ArrayList<Integer> capacity;
    private Location location;
    private final String address;
    private final String specialNotes;
    private final String phoneNumber;
    private String capacityString;
    private Restrictions restrictions;
    private int totalCapacity = 0;
    private int remainingCapacity = 0;
    private String shelterInfoString;

    private Shelter(int uniqueKey, String name, ArrayList<Integer> capacity, String restrictionsStr,
                    double[] longitudeLatitude, String address, String specialNotes,
                    String phoneNumber) {
        this.uniqueKey = uniqueKey;
        this.name = name;
        this.capacity = capacity;
        location = new Location(longitudeLatitude[0], longitudeLatitude[1]);
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
        restrictions = new Restrictions(restrictionsStr);
        for (int i = 0; i < capacity.size(); i++) {
            totalCapacity += capacity.get(i);
        }
        //remainingCapacity = totalCapacity;
        shelterInfoString = "Capacity: " + capacityString +"\n\nRemaining beds: "
                + remainingCapacity + "\n\n" + restrictions + "\n\n" + location + "\n\n"
                + address + "\n\n" + phoneNumber + "\n\nNote: " + specialNotes;
    }

    public Shelter(int uniqueKey, String name, ArrayList<Integer> capacity, int remainingCap,
                   String restrictions, double[] longitudeLatitude, String address,
                   String specialNotes, String phoneNumber) {

        this(uniqueKey, name, capacity, restrictions, longitudeLatitude, address, specialNotes,
                phoneNumber);
        remainingCapacity = remainingCap;
        shelterInfoString = "Capacity: " + capacityString +"\n\nRemaining beds: "
                + remainingCapacity + "\n\n" + restrictions + "\n\n" + location + "\n\n"
                + address + "\n\n" + phoneNumber + "\n\nNote: " + specialNotes;
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
    public String getLongitudeLatitudeString() { return location.toString(); }
    public String getNotes() { return specialNotes; }
    public String getSearchRestrictions() { return restrictions.getSearchRestrictions(); }
    public String getShelterInfoString() { return shelterInfoString; }

    public int getTotalCapacity() { return totalCapacity; }
    public int getRemainingCapacity() { return remainingCapacity; }

    public boolean canClaimBeds(int numBeds) {
        return !(numBeds > remainingCapacity);
    }
    public void claimBeds(int numBeds) {
        if (canClaimBeds(numBeds)) {
            remainingCapacity -= numBeds;
            shelterInfoString = "Capacity: " + capacityString +"\n\nRemaining beds: "
                    + remainingCapacity + "\n\n" + restrictions + "\n\n" + location + "\n\n"
                    + address + "\n\n" + phoneNumber + "\n\nNote: " + specialNotes;
        }
    }
    public boolean canReleaseBeds(int numBeds) {
        return !((numBeds + remainingCapacity) > totalCapacity);
    }
    public void releaseBeds(int numBeds) {
        if (canReleaseBeds(numBeds)) {
            remainingCapacity += numBeds;
            shelterInfoString = "Capacity: " + capacityString +"\n\nRemaining beds: "
                    + remainingCapacity + "\n\n" + restrictions + "\n\n" + location + "\n\n"
                    + address + "\n\n" + phoneNumber + "\n\nNote: " + specialNotes;
        }
    }
    public boolean allowsMen() {
        return restrictions.allowsMen();
    }
    public Location getLocation() {
        return location;
    }
}




