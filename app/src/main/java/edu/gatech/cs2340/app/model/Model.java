package edu.gatech.cs2340.app.model;

import android.util.Log;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import edu.gatech.cs2340.app.controller.MainActivity;

public class Model {
    private static final Model _instance = new Model();
    public static Model getInstance() { return _instance; }
    private final ArrayList<User> userDatabase = new ArrayList<>();
    private final ArrayList<Shelter> shelterDatabase = new ArrayList<>();
    private boolean readSDFile = false;

    /**
     * singleton pattern!
     */
    private Model() {

    }

    /**
     * Adds a shelter to an the array list of shelters
     * @param someShelter
     */
    private void addShelter(Shelter someShelter) {
        shelterDatabase.add(someShelter);
    }

    public ArrayList<Shelter> getShelters() {
        return shelterDatabase;
    }


    public Shelter findItemById(int id) {
        for (Shelter d : shelterDatabase) {
            if (d.getUniqueKey() == id) return d;
        }
        return null;
    }
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
    public void readSDFile(InputStream is) {
        if (readSDFile) {
            return; //lol no thanks
        }
        Model model = Model.getInstance();

        //From here we probably should call a model method and pass the InputStream
        //Wrap it in a BufferedReader so that we get the readLine() method
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        CSVReader reader = new CSVReader(br);
        String [] nextLine;
        try {
            reader.readNext(); //Throw away first line
            while ((nextLine = reader.readNext()) != null) {
                int uniqueKey = Integer.parseInt(nextLine[0]);
                String shelterName = nextLine[1];
                ArrayList<Integer> capacity = new ArrayList<>();
                Scanner sc = new Scanner(nextLine[2]);
                while (sc.hasNext()) {
                    while (sc.hasNextInt()) {
                        capacity.add(sc.nextInt());
                    }
                    if (sc.hasNext()) {
                        sc.next();
                    }
                }
                if (capacity.isEmpty()) {
                    capacity.add(-1);
                }
                String restrictions = nextLine[3];
                Double latitude = Double.parseDouble(nextLine[4]);
                Double longitude = Double.parseDouble(nextLine[5]);
                String address = nextLine[6];
                String specialNotes = nextLine[7];
                String phoneNumber = nextLine[8];

                model.addShelter(new Shelter(uniqueKey, shelterName, capacity,
                        restrictions, latitude, longitude, address, specialNotes, phoneNumber));
            }
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "error reading assets", e);
        }
        readSDFile = true;
    }
}
