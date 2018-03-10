package edu.gatech.cs2340.app.model;

/**
 * default user class
 * Created by Ethan on 2/16/2018.
 */

class User {
    private final String username;
    private final String password;
    private final boolean isAdmin;
    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        isAdmin = userType.equals("Admin");
    }
    public String getUsername() {
        return username;
    }
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    public boolean isAdmin() {
        return isAdmin;
    }
}
