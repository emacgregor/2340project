package edu.gatech.cs2340.app.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * default user class
 * Created by Ethan on 2/16/2018.
 */

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


    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.isAdmin = userType.equals("Admin");
    }

    public User() {
        super();
    }

    //getters
    public String getUsername() { return username; }
    public int getUid() { return uid; }
    public String getPassword() { return password; }
    public boolean getAdmin() {
        return isAdmin;
    }

    //setters
    public void setUid(int uid) { this.uid = uid; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

}
