package edu.gatech.cs2340.app.model;

import android.util.Log;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.gatech.cs2340.app.controller.MainActivity;

//Database Stuff

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;




public class Model {
    private static final Model _instance = new Model();
    public static Model getInstance() { return _instance; }
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
    public boolean addUser(String username, String password, String userType, AppDatabase db) {
        if (userExists(username, db)) {
            return false;
        }
        User nUser = new User(username, password, userType);
        db.userDao().insertAll(nUser);
        return true;
    }

    /**
     * Checks to see if the username was registered.
     * @param username The username we're looking for
     * @return Whether this username is registered.
     */
    public boolean userExists(String username, AppDatabase db) {
        List<String> userNames = db.userDao().getAllUsername();
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
     * @return Whether or not the username and password match.
     */
    public boolean checkCredentials(String username, String password, AppDatabase db) {
        List<User> users = db.userDao().getAllUsers();
        for (User user : users) {
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

    public void getSheltersFromDB() {
        if (readSDFile) {
            return; //lol no thanks
        }
        AsyncTask<Integer, Void, Void> asyncTask = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... movieIds) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://crossoutcancer.org/db_connect.php")
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);
                        int cap = object.getInt("bedCapacity");
                        ArrayList<Integer> capArray = new ArrayList<>(1);
                        capArray.add(cap);
                        Shelter shelter = new Shelter(object.getInt("id"), object.getString("name"), capArray
                                , object.getString("restrictions"), object.getDouble("longit"), object.getDouble("lat"), object.getString("address"), object.getString("specialNotes"), object.getString("phoneNumber"));

                        Model.getInstance().addShelter(shelter);
                        Log.d("Shelter", object.getString("name"));
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        asyncTask.execute();
        readSDFile = true;
    }

    public void updateShelterBeds() {

    }
}
