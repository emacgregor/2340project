package edu.gatech.cs2340.app.model;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Represents everything important happening with Shelters.
 */
@SuppressWarnings({"CyclicClassDependency", "ClassWithTooManyDependents"})
public class Shelter {
    private final int uniqueKey;
    private final Location location;
    private String capacityString;
    private final Restrictions restrictions;
    private int totalCapacity = 0;
    private int remainingCapacity = 0;
    private String shelterInfoString;
    private final ShelterInfo info;

    /**
     * Constructor for Shelter but missing remainingCap
     * @param uniqueKey The shelter's ID number.
     * @param capacity The shelter's capacity in list form for different types.
     * @param longitudeLatitude A double array containing longitude and latitude.
     * @param info Holds information on name, restrictions, phoneNumber, specialNotes, and address.
     */
    private Shelter(int uniqueKey, ArrayList<Integer> capacity, double[] longitudeLatitude,
                    ShelterInfo info) {
        this.uniqueKey = uniqueKey;
        location = new Location(longitudeLatitude[0], longitudeLatitude[1]);
        this.info = new ShelterInfo(info);

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

        restrictions = new Restrictions(info.getRestrictions());

        for (int i = 0; i < capacity.size(); i++) {
            totalCapacity += capacity.get(i);
        }
        remainingCapacity = totalCapacity;
    }

    /**
     * Constructor for Shelter with all unique fields.
     * @param uniqueKey The shelter's ID number.
     * @param capacity The shelter's capacity in list form for different types.
     * @param remainingCap The shelter's remaining capacity.
     * @param longitudeLatitude A double array containing longitude and latitude.
     * @param info Holds information on name, restrictions, phoneNumber, specialNotes, and address.
     */
    public Shelter(int uniqueKey, ArrayList<Integer> capacity, int remainingCap,
                   double[] longitudeLatitude, ShelterInfo info) {

        this(uniqueKey, capacity, longitudeLatitude, info);
        remainingCapacity = remainingCap;
        updateShelterInfoString();
    }


    /**
     * Says whether numBeds can be claimed
     * @param numBeds Number of beds
     * @return Whether numBeds can be claimed
     */
    public boolean canClaimBeds(int numBeds) {
        return !(numBeds > remainingCapacity);
    }

    /**
     * Claims numBeds beds.
     * @param numBeds the number of beds being claimed.
     */
    public void claimBeds(int numBeds) {
        if (canClaimBeds(numBeds)) {
            remainingCapacity -= numBeds;
            updateShelterInfoString();
        } else {
            ShelterDatabase.updateFailureString(
                    " This shelter does not have that many beds to spare.");
        }
    }

    /**
     * Says whether numBeds can be released
     * @param numBeds Number of beds
     * @return Whether numBeds can be released
     */
    public boolean canReleaseBeds(int numBeds) {
        return !((numBeds + remainingCapacity) > totalCapacity);
    }

    /**
     * Releases numBeds beds.
     * @param numBeds beds being released.
     */
    public void releaseBeds(int numBeds) {
        if (canReleaseBeds(numBeds)) {
            remainingCapacity += numBeds;
            updateShelterInfoString();
        }
    }

    /**
     * Makes a data element with shelter details for class data manager.
     * @return The new data element.
     */
    public DataElement makeDataElement() {
        return new DataElement(getName(), getAddress() + "\n" + getPhoneNumber(),
                getLocation(), getRestrictions());
    }
    private void updateShelterInfoString() {
        shelterInfoString = "Capacity: " + capacityString +"\n\nRemaining beds: "
                + remainingCapacity + "\n\n" + restrictions + "\n\n" + location + "\n\n"
                + getAddress() + "\n\n" + getPhoneNumber() + "\n\nNote: " + getNotes();
    }

    public String toString() {
        return info.getName() + " " + info.getAddress();
    }

    /**
     * Getter for unique key.
     * @return uniqueKey
     */
    public int getUniqueKey() {return uniqueKey; }
    /**
     * Getter for name.
     * @return name
     */
    public String getName() { return info.getName(); }
    /**
     * Getter for address
     * @return address
     */
    private String getAddress() { return info.getAddress(); }
    /**
     * Getter for phone number
     * @return phoneNumber
     */
    private String getPhoneNumber() { return info.getPhoneNumber(); }
    /**
     * Getter for notes
     * @return notes
     */
    private String getNotes() { return info.getSpecialNotes(); }
    /**
     * Getter for searchRestrictions string.
     * @return searchRestrictions
     */
    public CharSequence getSearchRestrictions() { return restrictions.getSearchRestrictions(); }
    /**
     * Getter for restrictions as a whole
     * @return restrictions
     */
    private Restrictions getRestrictions() { return  restrictions; }
    /**
     * Getter for shelter info as a string.
     * @return shelterInfoString
     */
    public String getShelterInfoString() { return shelterInfoString; }

    /**
     * Getter for total capacity.
     * @return totalCapacity
     */
    public int getTotalCapacity() { return totalCapacity; }
    /**
     * Getter for remaining capacity
     * @return remainingCapacity
     */
    public int getRemainingCapacity() { return remainingCapacity; }
    /**
     * Returns this shelter's location object.
     * @return location
     */
    private Location getLocation() {
        return location;
    }

    /**
     * Returns whether this shelter allows men.
     * @return whether this shelter allows men.
     */
    public boolean allowsMen() {
        return restrictions.allowsMen();
    }

    /**
     * Returns a string that contains all to do with searching
     * @return The name and search restrictions concatenated and lower cased.
     */
    public String getSearchTerms() {
        return (getSearchRestrictions() +  " " + getName()).toLowerCase(Locale.getDefault());
    }
}




