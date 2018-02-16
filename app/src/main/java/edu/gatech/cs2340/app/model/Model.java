package edu.gatech.cs2340.app.model;

import java.util.ArrayList;

/**
 * Created by ethan_7416xi5 on 2/16/2018.
 */

public class Model {
    private static final Model _instance = new Model();
    public static Model getInstance() { return _instance; }
    private ArrayList<String> credentials = new ArrayList<String>();

    /**
     * Adds a new user to the database.
     * @param newUser The user that will be added to the credential list. Username + password
     *                separated by ":"
     * @return Whether the user got added (whether that username was already registered.)
     */
    public boolean addUser(String newUser) {
        if (userExists(newUser.split(":")[0])) {
            return false;
        }
        credentials.add(newUser);
        return true;
    }

    /**
     * Checks to see if the username was registered.
     * @param checkUser The username we're looking for
     * @return Whether this username is registered.
     */
    public boolean userExists(String checkUser) {
        for (String credential : credentials) {
            if (checkUser.equals(credential.split(":")[0])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the username and password match.
     * @param checkUser The username + password combo we're looking for separated by ":"
     * @return Whether or not the username and password match.
     */
    public boolean correctUser(String checkUser) {
        return credentials.contains(checkUser);
    }
}
