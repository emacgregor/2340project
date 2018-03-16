package edu.gatech.cs2340.app.model;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.OutputStreamWriter;
import android.net.Uri;
import java.io.OutputStream;
import java.io.BufferedWriter;

public final class Model {
    private static final Model _instance = new Model();
    public static Model getInstance() { return _instance; }
    private final ArrayList<Shelter> shelterDatabase = new ArrayList<>();
    private boolean readSDFile = false;
    private User currentUser;
    private Shelter currentShelter;
    private String failureString;

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
            if (d.getUniqueKey() == id) {
                return d;
            }
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

    private static class dbReaderTask extends AsyncTask<Integer, Void, Void> {
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
                    Shelter shelter = new Shelter(object.getInt("id"),
                            object.getString("name"), capArray,
                            object.getInt("remainingCap"),
                            object.getString("restrictions"), object.getDouble("longit"),
                            object.getDouble("lat"), object.getString("address"),
                            object.getString("specialNotes"),
                            object.getString("phoneNumber"));

                    Model.getInstance().addShelter(shelter);
                    Log.d("Shelter", object.getString("name"));
                }


            } catch (IOException|JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public void getSheltersFromDB() {
        if (readSDFile) {
            return; //lol no thanks
        }
        dbReaderTask dbReader = new dbReaderTask();
        dbReader.execute();
        readSDFile = true;
    }

    private final class dbUpdateTask extends AsyncTask<Integer, Void, Void> {
        final String url_update = "http://crossoutcancer.org/updateShelter.php";
        final int shelterID;
        private dbUpdateTask(int shelterID) {
            this.shelterID = shelterID;
        }

        /**
         * Saving product
         */
        @Override
        protected Void doInBackground(Integer... integers) {
                    /*int sid = entry.getKey();
                    int beds = entry.getValue();*/
            int remainingCapacity = shelterDatabase.get(shelterID).getRemainingCapacity();
            int beds = shelterDatabase.get(shelterID).getTotalCapacity() - remainingCapacity;

            HttpURLConnection connection = null;
            //OutputStreamWriter request = null;

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

                assert connection != null;
                int response_code = connection.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line = reader.readLine();

                    while (line != null) {
                        result.append(line);
                        line = reader.readLine();
                    }

                    // Pass data to onPostExecute method
                    Log.d("Success", "Success");

                } else {
                    Log.d("Fail", "Fail");

                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("exception", "exception");
            }
            return null;
        }
    }
    private void updateCurrentShelterBeds(final int shelterID) {
        dbUpdateTask dbUpdater = new dbUpdateTask(shelterID);
        dbUpdater.execute();
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
