package edu.gatech.cs2340.app.model;

/**
 * Created by robertwaters on 3/13/18.
 * Information Holder
 * Primary responsibility is to maintain all the data about a single thing
 */

public class DataElement {
    private static int Next_ID = 1000;
    @SuppressWarnings("FieldCanBeLocal")
    private final int _id;
    private final String _name;
    private final String _description;
    private final Location _location;
    private final Restrictions _restrictions;

    /**
     * Create new element
     * @param name   the name of the element
     * @param desc   a textual description
     * @param location  the location of the element
     */
    public DataElement(String name, String desc, Location location, Restrictions restrictions) {
        _name = name;
        _description = desc;
        _location = location;
        _id = Next_ID++;
        _restrictions = restrictions;
    }

    @Override
    public String toString() {
        return  _name + "\n" + _description;
    }

    /*
     Getters for the data elements
     */
    public String getName() { return _name;}
    public String getDescription() {  return _description; }

    public double getLatitude() { return _location.getLatitude(); }
    public double getLongitude() { return _location.getLongitude(); }

    public Restrictions getRestrictions() {
        return _restrictions;
    }

}
