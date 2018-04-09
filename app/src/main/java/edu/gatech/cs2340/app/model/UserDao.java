package edu.gatech.cs2340.app.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * The UserDao is a data access object for the User Room Persistence library
 * This object contains the queries to be used by the Room Database to fetch
 * or set the desired data.
 *
 */
@SuppressWarnings({"unused", "CyclicClassDependency"})
@Dao
public interface UserDao {

    /**
     * This is an SQLite query to get numBeds from the desired user
     * @param name - the username to search
     * @return the queried data (numBeds) from the user class of the same name
     */
    @Query("SELECT numBedsClaimed FROM user WHERE username LIKE :name")
    int getBeds(String name);

    /**
     *  This is an SQLite query to get shelterID from the desired user
     * @param name - the username to search
     * @return the queried data (shelterID) from the user class of the same name
     */
    @Query("SELECT shelterID FROM user WHERE username LIKE :name")
    int findShelterID(String name);

    /**
     * This is an SQLite query to return a USER class from the database
     * @param name - the username to search
     * @return the queried data (a USER) from the user database of the same name
     */
    @Query("SELECT * FROM user WHERE username LIKE :name")
    User findByUsername(String name);

    /**
     * This is an SQLite query that returns all usernames from all users in the database
     * @return all usernames in the database
     */
    @Query("SELECT username FROM user")
    List<String> getAllUsername();

    /**
     * This is an SQLite query to get all USERS from the database
     * @return all USERS in the database
     */
    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    /**
     * This is an SQLite insert statement that inserts all users into the database
     * @param users - the user(s) to be inserted into the database
     */
    @Insert
    void insertAll(User... users);
}
