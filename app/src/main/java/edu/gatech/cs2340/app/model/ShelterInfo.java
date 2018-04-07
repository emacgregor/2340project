package edu.gatech.cs2340.app.model;

/**
 * Contains important String information for passing Shelters.
 */

class ShelterInfo {
    private final String name;
    private final String address;
    private final String specialNotes;
    private final String phoneNumber;
    private final String restrictions;

    /**
     * Constructor for shelterInfo containing all data being held.
     * @param name name
     * @param address address
     * @param specialNotes notes
     * @param phoneNumber phone number
     * @param restrictions restrictions
     */
    public ShelterInfo(String name, String address, String specialNotes, String phoneNumber,
                       String restrictions) {
        this.name = name;
        this.address = address;
        this.specialNotes = specialNotes;
        this.phoneNumber = phoneNumber;
        this.restrictions = restrictions;
    }

    /**
     * Makes a new shelterInfo object from supplied shelterInfo
     * @param info other shelterInfo
     */
    public ShelterInfo(ShelterInfo info) {
        this(info.getName(), info.getAddress(), info.getSpecialNotes(),
                info.getPhoneNumber(), info.getRestrictions());
    }

    /**
     * Returns the shelter's name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the shelter's address
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the shelter's notes
     * @return specialNotes
     */
    public String getSpecialNotes() {
        return specialNotes;
    }

    /**
     * Returns the shelter's phone number.
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the shelter's restriction's string.
     * @return restrictions
     */
    public String getRestrictions() {
        return restrictions;
    }
}
