package edu.gatech.cs2340.app.model;

import java.util.List;

/**
 * This is the main logic class of the entire app.
 */
@SuppressWarnings({"UtilityClass", "CyclicClassDependency"})
public final class Model {
    private static User currentUser;
    private static Restrictions mapRestrictions;

    /**
     * singleton pattern!
     */
    private Model() {

    }

    /**
     * Adds a new user to the database.
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @param userType The user type of the new user (admin, user).
     * @param db The database the user is being added to.
     * @return Whether the user got added (whether that username was already registered.)
     */
    public static boolean addUser(String username, String password, String userType,
                                  AppDatabase db) {
        if (userExists(username, db)) {
            return false;
        }
        User nUser = new User(username, password, userType);
        db.insertAll(nUser);
        setCurrentUser(nUser);
        return true;
    }

    /**
     * Checks to see if the username was registered.
     * @param username The username we're looking for
     * @param db The database we're looking for the user in.
     * @return Whether this username is registered.
     */
    public static boolean userExists(String username, AppDatabase db) {
        List<String> userNames = db.getAllUsername();
        for (String s : userNames) {
            if (username.equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the username and password match.
     * @param username The username that must be matched.
     * @param password The password that must be matched.
     * @param db The database the user is being matched in.
     * @return Whether or not the username and password match.
     */
    public static boolean checkCredentials(String username, String password, AppDatabase db) {
        List<User> users = db.getAllUsers();
        for (User user : users) {
            if (username.equals(user.getUsername()) && user.isBanned() != 1) {
                if (user.checkPassword(password)) {
                    setCurrentUser(user);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /**
     * Changes the currentUser variable which is the user currently signed in.
     * @param user The user who will be the new currentUser.
     */
    private static void setCurrentUser(User user) {
        currentUser = user;
    }

// --Commented out by Inspection START (4/8/2018 17:23):
//    /**
//     * Returns the current signed in user.
//     * @return The currently signed in user.
//     */
//    public static User getCurrentUser() {
//        return currentUser;
//    }
// --Commented out by Inspection STOP (4/8/2018 17:23)

    /**
     * Sets the mapRestrictions variable (used for checking which shelters belong on map)
     * @param mRestrictions The new mapRestrictions.
     */
    public static void setMapRestrictions(Restrictions mRestrictions) {
        mapRestrictions = mRestrictions;
    }

    /**
     * Gets the map restrictions (used for checking shelters on map)
     * @return The map restrictions.
     */
    public static Restrictions getMapRestrictions() {
        return mapRestrictions;
    }

    /**
     * Finds whether two Restrictions have a match
     * @param r1 First Restrictions
     * @param r2 Second Restrictions
     * @return Whether there was a match
     */
    public static boolean hasMatch(Restrictions r1, Restrictions r2) {
        return r1.hasMatch(r2);
    }

    /**
     * Returns whether the current user can claim beds at the specified shelter.
     * @param shelterID The shelterID
     * @return Whether the user can claim the beds.
     */
    public static boolean currentUserCanClaimBeds(int shelterID) {
        return currentUser.canClaimBeds(shelterID);
    }

    /**
     * Returns whether the current user can release beds at the specified shelter.
     * @param numBeds The number of beds
     * @param shelterID The shelterID
     * @return Whether the user can release the beds.
     */
    public static boolean currentUserCanReleaseBeds(int numBeds, int shelterID) {
        return currentUser.canReleaseBeds(numBeds, shelterID);
    }
    /**
     * Claim beds at the specified shelter.
     * @param numBeds number of beds
     * @param shelterID The shelterID
     */
    public static void currentUserClaimBeds(int numBeds, int shelterID) {
        currentUser.claimBeds(numBeds, shelterID);
    }

    /**
     * Release beds at the specified shelter.
     * @param numBeds The number of beds
     * @param shelterID The shelterID
     */
    public static void currentUserReleaseBeds(int numBeds, int shelterID) {
       currentUser.releaseBeds(numBeds, shelterID);
    }

    public static boolean currentUserIsAdmin() {
        return currentUser.getAdmin();
    }
}
