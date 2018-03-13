package edu.gatech.cs2340.app.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

/**
 * Created by Hyland on 3/13/2018.
 */

@Dao
public interface UserDao {

    @Insert
    void insertAll(User... users);

}
