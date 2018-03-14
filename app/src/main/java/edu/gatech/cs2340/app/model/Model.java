package edu.gatech.cs2340.app.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.opencsv.CSVReader;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.gatech.cs2340.app.controller.MainActivity;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.net.*;
import java.io.OutputStreamWriter;
import android.net.Uri;
import java.io.OutputStream;
import java.io.BufferedWriter;





public class Model {
    private static final Model _instance = new Model();
    public static Model getInstance() { return _instance; }
    private final ArrayList<Shelter> shelterDatabase = new ArrayList<>();
    private boolean readSDFile = false;
    private User currentUser;
    private Shelter currentShelter;
    private String failureString;

    private static final String TAG_SUCCESS = "success";

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
        setCurrentUser(nUser);
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
                if (user.checkPassword(password)) {
                    setCurrentUser(user);
                    return true;
                }
                return false;
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
                                , object.getInt("remainingCap"), object.getString("restrictions"),
                                object.getDouble("longit"), object.getDouble("lat"),
                                object.getString("address"), object.getString("specialNotes"),
                                object.getString("phoneNumber"));

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

    public void updateShelterBeds(AppDatabase db) {
        final String url_update = "http://crossoutcancer.org/updateShelter.php";
        List<User> userList = db.userDao().getAllUsers();
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (User current: userList) {
            int id = current.getShelterID();
            int beds = current.getNumBedsClaimed();
            if (map.containsKey(id)){
                beds = map.get(id) + beds;
                map.put(id, beds);
            } else {
                map.put(id, beds);
            }

        }

        final Map<Integer, Integer> usermap = map;

        AsyncTask<Integer, Void, Void> asyncTask = new AsyncTask<Integer, Void, Void>() {
            /**
             * Before starting background thread Show Progress Dialog
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            /**
             * Saving product
             */
            protected Void doInBackground(Integer... integers) {

                for (Map.Entry<Integer, Integer> entry : usermap.entrySet()) {
                    int sid = entry.getKey();
                    int beds = entry.getValue();

                    OkHttpClient client = new OkHttpClient();

                    HttpUrl.Builder urlBuilder = HttpUrl.parse(url_update).newBuilder();
                    urlBuilder.addQueryParameter("id", String.valueOf(sid));
                    urlBuilder.addQueryParameter("beds", String.valueOf(beds));
                    //urlBuilder.addQueryParameter("shelterBeds", String.valueOf(shelterBeds));
                    String url = urlBuilder.build().toString();


                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    try {
                        client.newCall(request).execute();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
                return null;
            }
        };
        asyncTask.execute();
    }
    public void updateCurrentShelterBeds(final int shelterID) {
        final String url_update = "http://crossoutcancer.org/updateShelter.php";


        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> asyncTask = new AsyncTask<Integer, Void, Void>() {
            /**
             * Before starting background thread Show Progress Dialog
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            /**
             * Saving product
             */
            protected Void doInBackground(Integer... integers) {
                    /*int sid = entry.getKey();
                    int beds = entry.getValue();*/
                int sid = shelterID;
                int remainingCapacity = shelterDatabase.get(shelterID).getRemainingCapacity();
                int beds = shelterDatabase.get(shelterID).getTotalCapacity() - remainingCapacity;

                HttpURLConnection connection = null;
                OutputStreamWriter request = null;

                try {
                    URL url = new URL(url_update);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestMethod("POST");


                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("id", String.valueOf(shelterID))
                            .appendQueryParameter("remainingCap", String.valueOf(remainingCapacity))
                            .appendQueryParameter("bed", String.valueOf(beds));
                    String query = builder.build().getEncodedQuery();
                    Log.d("Query", query);

                    // Open connection for sending data
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                    connection.connect();

                } catch (IOException e) {
                    // Error
                    Log.d("what happened", "ahhhhhhh");
                }
                try {

                    int response_code = connection.getResponseCode();

                    // Check if successful connection made
                    if (response_code == HttpURLConnection.HTTP_OK) {

                        // Read data sent from server
                        InputStream input = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder result = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        // Pass data to onPostExecute method
                        Log.d("Success", "Success");

                    }else{
                        Log.d("Fail", "Fail");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("exception", "exception");
                }
                return null;
            }

        };
        asyncTask.execute();
    }
    public void setCurrentUser(User user) {
        currentUser = user;
    }
    public User getCurrentUser() {
        return  currentUser;
    }
    public boolean claimBeds(int numBeds, int shelterID) {
        if (currentUser.canClaimBeds(shelterID)
                && shelterDatabase.get(shelterID).canClaimBeds(numBeds)) {
            currentUser.claimBeds(numBeds, shelterID);
            shelterDatabase.get(shelterID).claimBeds(numBeds);
            updateCurrentShelterBeds(shelterID);
            return true;
        } else {
            failureString = "You cannot claim these beds.";
            if (!currentUser.canClaimBeds(shelterID)) {
                failureString += " You already own beds at "
                        + shelterDatabase.get(currentUser.getShelterID()).getName() + ".";
            }
            if (!shelterDatabase.get(shelterID).canClaimBeds(numBeds)) {
                failureString += " This shelter does not have that many beds to spare.";
            }
            return false;
        }
    }
    public String getFailureString() {
        return failureString;
    }
    public boolean releaseBeds(int numBeds, int shelterID) {
        if (currentUser.canReleaseBeds(numBeds, shelterID)
                && shelterDatabase.get(shelterID).canReleaseBeds(numBeds)) {
            currentUser.releaseBeds(numBeds, shelterID);
            shelterDatabase.get(shelterID).releaseBeds(numBeds);
            updateCurrentShelterBeds(shelterID);
            return true;
        } else {
            failureString = "You cannot release beds at this shelter:";
            if (currentUser.getShelterID() == -1) {
                failureString += " You do not own any beds.";
            } else if (currentUser.getShelterID() != shelterID) {
                failureString += " Your beds are from "
                        + shelterDatabase.get(currentUser.getShelterID()).getName() + ".";
            }
            if (currentUser.getNumBedsClaimed() < numBeds) {
                failureString += " You do not have this many beds.";
            }
            return false;
        }
    }
    public void setCurrentShelter(Shelter newShelter) {
        currentShelter = newShelter;
    }
    public Shelter getCurrentShelter() {
        return currentShelter;
    }
}
