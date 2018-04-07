package edu.gatech.cs2340.app.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@SuppressWarnings("unused")
@Dao
public interface UserDao {

    @Query("SELECT numBedsClaimed FROM user WHERE username LIKE :name")
    int getBeds(String name);

    @Query("SELECT shelterID FROM user WHERE username LIKE :name")
    int findShelterID(String name);

    @Query("SELECT * FROM user WHERE username LIKE :name")
    User findByUsername(String name);

    @Query("SELECT username FROM user")
    List<String> getAllUsername();

    @Query("SELECT * FROM user")
    List<User> getAllUsers();


    @Insert
    void insertAll(User... users);
}
