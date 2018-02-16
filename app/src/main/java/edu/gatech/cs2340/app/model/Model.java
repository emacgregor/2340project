package edu.gatech.cs2340.app.model;

import java.util.ArrayList;

/**
 * Created by ethan_7416xi5 on 2/16/2018.
 */

public class Model {
    private static final Model _instance = new Model();
    public static Model getInstance() { return _instance; }
    private ArrayList<User> userDatabase = new ArrayList<User>();

    /**
     * Adds a new user to the database.
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @param userType The user type of the new user (admin, user).
     * @return Whether the user got added (whether that username was already registered.)
     */
    public boolean addUser(String username, String password, String userType) {
        if (userExists(username)) {
            return false;
        }
        userDatabase.add(new User(username, password, userType));
        return true;
    }

    /**
     * Checks to see if the username was registered.
     * @param username The username we're looking for
     * @return Whether this username is registered.
     */
    public boolean userExists(String username) {
        for (User user : userDatabase) {
            if (username.equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the username and password match.
     * @param username The username that must be matched.
     * @param password The password that must be matched.
     * @return Whether or not the username and password match.
     */
    public boolean checkCredentials(String username, String password) {
        for (User user : userDatabase) {
            if (username.equals(user.getUsername())) {
                return user.checkPassword(password);
            }
        }
        return false;
    }
}
