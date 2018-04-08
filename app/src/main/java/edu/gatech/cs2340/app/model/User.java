package edu.gatech.cs2340.app.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Holds all information about users in a database.
 */

@SuppressWarnings("CyclicClassDependency")
@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "isAdmin")
    private boolean isAdmin;

    @ColumnInfo(name = "shelterID")
    private int shelterID;

    @ColumnInfo(name = "numBedsClaimed")
    private int numBedsClaimed;

    /**
     * Constructor that takes in information regarding the user details.
     * @param username The user's username
     * @param password The user's password
     * @param userType The type of the user.
     */
    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.isAdmin = "Admin".equals(userType);
        this.shelterID = -1;
        this.numBedsClaimed = 0;
    }

    /**
     * Used for dao I think?
     */
    public User() {
        super();
    }

    /**
     * Checks whether the user's password is correct.
     * @param password The password to be checked
     * @return Whether the password was correct.
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Whether this user can claim beds at this shelter.
     * @param shelterID The shelter being claimed at
     * @return Whether they can
     */
    public boolean canClaimBeds(int shelterID) {
        return ((this.shelterID == -1) || (this.shelterID == shelterID));
    }

    /**
     * Claims beds for user.
     * @param numBeds Number of beds to be claimed
     * @param shelterID Shelter they are being claimed at.
     */
    public void claimBeds(int numBeds, int shelterID) {
        if (!canClaimBeds(shelterID)) {
            ShelterDatabase.updateFailureString(" You already own beds at " +
                    ShelterDatabase.getNameByID(getShelterID()) + ".");
            return;
        }
        this.shelterID = shelterID;
        numBedsClaimed += numBeds;
    }

    /**
     * Returns whether beds can be released.
     * @param numBeds Number of beds to be released
     * @param shelterID Shelter they are being released at
     * @return Whether they can be released.
     */
    public boolean canReleaseBeds(int numBeds, int shelterID) {
        return ((numBedsClaimed >= numBeds) && (this.shelterID == shelterID));
    }
    /**
     * Releases beds for user.
     * @param numBeds Number of beds to be released
     * @param shelterID Shelter they are being released at
     */
    public void releaseBeds(int numBeds, int shelterID) {
        if (canReleaseBeds(numBeds, shelterID)) {
            numBedsClaimed -= numBeds;
            if (numBedsClaimed == 0) {
                this.shelterID = -1;
            }
        } else {
            String moreFailure = "";
            if (getShelterID() == -1) {
                moreFailure = " You do not own any beds.";
            } else if (getShelterID() != shelterID) {
                moreFailure = " Your beds are from "
                        + ShelterDatabase.getNameByID(getShelterID()) + ".";
            }
            if (getNumBedsClaimed() < numBeds) {
                moreFailure += " You do not have this many beds.";
            }
            ShelterDatabase.updateFailureString(moreFailure);
        }
    }

    /**
     * Return the shelterID of the shelter the user has claimed beds at
     * @return shelterID
     */
    private int getShelterID() {
        return shelterID;
    }

    /**
     * Gets number of beds the user has claimed.
     * @return numBedsClaimed
     */
    private int getNumBedsClaimed() {
        return numBedsClaimed;
    }

    /**
     * Sets shelterId
     * @param shelterID Shelter the user has claimed beds at.
     */
    public void setShelterID(int shelterID) {
        this.shelterID = shelterID;
    }

    /**
     * Sets the number of beds the user has claimed.
     * @param numBedsClaimed Number of beds the user has claimed.
     */
    public void setNumBedsClaimed(int numBedsClaimed) {
        this.numBedsClaimed = numBedsClaimed;
    }

    /**
     * Gets username.
     * @return username
     */
    public String getUsername() { return username; }

    /**
     * Gets user ID.
     * @return uid
     */
    public int getUid() { return uid; }

    /**
     * Gets password
     * @return password
     */
    public String getPassword() { return password; }

    /**
     * Gets whether the user is an admin.
     * @return isAdmin
     */
    public boolean getAdmin() {
        return isAdmin;
    }

    /**
     * Sets the user's id.
     * @param uid The user's new id.
     */
    public void setUid(int uid) { this.uid = uid; }

    /**
     * Sets the user's username.
     * @param username The new username.
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Sets the user's password
     * @param password The new password
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Sets whether the user is an admin.
     * @param admin The new admin status.
     */
    public void setAdmin(boolean admin) { isAdmin = admin; }
}
