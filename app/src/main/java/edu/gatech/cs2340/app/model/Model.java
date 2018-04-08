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

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.OutputStreamWriter;
import android.net.Uri;
import java.io.OutputStream;
import java.io.BufferedWriter;

/**
 * This is the main logic class of the entire app.
 */
@SuppressWarnings({"UtilityClass", "CyclicClassDependency"})
public final class Model {
    private static final ArrayList<Shelter> shelterDatabase = new ArrayList<>();
    private static boolean readSDFile = false;
    private static User currentUser;
    private static Shelter currentShelter;
    private static String failureString;
    private static Restrictions mapRestrictions;

    /**
     * singleton pattern!
     */
    private Model() {

    }

    /**
     * Adds a shelter to an the array list of shelters
     * @param someShelter This shelter gets added to the shelter database.
     */
    private static void addShelter(Shelter someShelter) {
            shelterDatabase.add(someShelter);
    }

    /**
     * Returns the local database of shelters.
     * @return The local database of shelters.
     */
    public static ArrayList<Shelter> getShelters() {
        getSheltersFromDB();
        return shelterDatabase;
    }


    /**
     * Finds a shelter based on the id provided.
     * @param id The id we're looking for.
     * @return The shelter of the id we found.
     */
    public static Shelter findItemById(int id) {
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
     * @param db The database the user is being added to.
     * @return Whether the user got added (whether that username was already registered.)
     */
    public static boolean addUser(String username, String password, String userType,
                                  AppDatabase db) {
        if (userExists(username, db)) {
            return false;
        }
        User nUser = new User(username, password, userType);
        db.insertAll(nUser);
        setCurrentUser(nUser);
        return true;
    }

    /**
     * Checks to see if the username was registered.
     * @param username The username we're looking for
     * @param db The database we're looking for the user in.
     * @return Whether this username is registered.
     */
    public static boolean userExists(String username, AppDatabase db) {
        List<String> userNames = db.getAllUsername();
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
     * @param db The database the user is being matched in.
     * @return Whether or not the username and password match.
     */
    public static boolean checkCredentials(String username, String password, AppDatabase db) {
        List<User> users = db.getAllUsers();
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

    @SuppressWarnings("CyclicClassDependency")
    private static class dbReaderTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... movieIds) {

            Call.Factory client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder = builder.url("https://2340project.000webhostapp.com/db_connect.php");
            Request request = builder.build();

            try {
                Call call = client.newCall(request);
                Response response = call.execute();

                ResponseBody responseBody = response.body();
                JSONArray array = new JSONArray(responseBody.string());
                shelterDatabase.clear();

                for (int i = 0; i < array.length(); i++) {

                    JSONObject object = array.getJSONObject(i);
                    int cap = object.getInt("bedCapacity");

                    ArrayList<Integer> capArray = new ArrayList<>();
                    capArray.add(cap);
                    double[] longitudeLatitude = {object.getDouble("longit"),
                            object.getDouble("lat")};
                    Shelter shelter = new Shelter(object.getInt("id"), capArray,
                            object.getInt("remainingCap"), longitudeLatitude,
                            new ShelterInfo(object.getString("name"),
                                    object.getString("address"),
                                    object.getString("specialNotes"),
                                    object.getString("phoneNumber"),
                                    object.getString("restrictions")));

                    addShelter(shelter);
                    Shelter thisShelter = shelterDatabase.get(i);
                    Log.d("Shelter", object.getString("remainingCap"));
                    Log.d("ShelterObject", String.valueOf(thisShelter.getRemainingCapacity()));
                    Log.d("ShelterObject", thisShelter.getShelterInfoString());

                }


            } catch (IOException|JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Gets shelters from the online database. See dbReaderTask for more info.
     */
    public static void getSheltersFromDB() {
        if (readSDFile) {
            return; //lol no thanks
        }
        dbReaderTask dbReader = new dbReaderTask();
        dbReader.execute();
        readSDFile = true;
    }

    @SuppressWarnings({"MismatchedQueryAndUpdateOfStringBuilder", "CyclicClassDependency"})
    private static final class dbUpdateTask extends AsyncTask<Integer, Void, Void> {
        final String url_update = "https://2340project.000webhostapp.com/updateShelter.php";
        final int shelterID;
        private dbUpdateTask(int shelterID) {
            this.shelterID = shelterID;
        }

        /**
         * Saving product
         */
        @SuppressWarnings("OverlyLongMethod")
        @Override
        protected Void doInBackground(Integer... integers) {
            Shelter shelter = shelterDatabase.get(shelterID);
            int remainingCapacity = shelter.getRemainingCapacity();
            int beds = shelter.getTotalCapacity() - remainingCapacity;

            HttpURLConnection connection = null;

            try {
                URL url = new URL(url_update);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder();
                builder = builder.appendQueryParameter("id", String.valueOf(shelterID));
                builder = builder.appendQueryParameter("remainingCap",
                        String.valueOf(remainingCapacity));
                builder = builder.appendQueryParameter("bed", String.valueOf(beds));
                /*Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", String.valueOf(shelterID))
                        .appendQueryParameter("remainingCap", String.valueOf(remainingCapacity))
                        .appendQueryParameter("bed", String.valueOf(beds));*/
                Uri uri = builder.build();
                String query = uri.getEncodedQuery();
                //Log.d("Query", query);

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
                e.printStackTrace();
                // Error
                //Log.d("what happened", "ahhhhhhh");
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
                    //Log.d("Success", "Success");
                }
            } catch (IOException e) {
                e.printStackTrace();
                //Log.d("exception", "exception");
            }
            return null;
        }
    }
    private static void updateCurrentShelterBeds(final int shelterID) {
        dbUpdateTask dbUpdater = new dbUpdateTask(shelterID);
        dbUpdater.execute();
    }

    /**
     * Changes the currentUser variable which is the user currently signed in.
     * @param user The user who will be the new currentUser.
     */
    private static void setCurrentUser(User user) {
        currentUser = user;
    }

// --Commented out by Inspection START (4/7/2018 16:56):
//    /**
//     * Returns the current signed in user.
//     * @return The currently signed in user.
//     */
//    public User getCurrentUser() {
//        return  currentUser;
//    }
// --Commented out by Inspection STOP (4/7/2018 16:56)

    /**
     * Provides all the logic for claiming beds with the current user and the provided shelter.
     * @param numBeds The number of beds that are being claimed.
     * @param shelterID The shelter that beds are being claimed in.
     * @return Whether the beds were claimed.
     */
    private static boolean claimBeds(int numBeds, int shelterID) {
        failureString = "You cannot claim these beds.";
        Shelter shelter = shelterDatabase.get(shelterID);
        if (currentUser.canClaimBeds(shelterID)
                && shelter.canClaimBeds(numBeds)) {
            currentUser.claimBeds(numBeds, shelterID);
            shelter.claimBeds(numBeds);
            updateCurrentShelterBeds(shelterID);
            return true;
        }
        return false;
    }
    /**
     * Overloaded for claiming beds
     * @param numBeds The number of beds that are being claimed.
     * @return Whether the beds were claimed.
     */
    public static boolean claimBeds(int numBeds) {
        return claimBeds(numBeds, getCurrentKey());
    }

    /**
     * This returns the String that provides the reasons why claimBeds or releaseBeds failed.
     * @return Above described String.
     */
    public static CharSequence getFailureString() {
        return failureString;
    }

    /**
     * Provides all the logic for releasing beds with the current user and the provided shelter.
     * @param numBeds The number of beds that are being released.
     * @param shelterID The shelter that beds are being released in.
     * @return Whether the beds were released.
     */
    private static boolean releaseBeds(int numBeds, int shelterID) {
        failureString = "You cannot release beds at this shelter:";
        Shelter shelter = shelterDatabase.get(shelterID);
        if (currentUser.canReleaseBeds(numBeds, shelterID)
                && shelter.canReleaseBeds(numBeds)) {
            currentUser.releaseBeds(numBeds, shelterID);
            shelter.releaseBeds(numBeds);
            updateCurrentShelterBeds(shelterID);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Overloaded for releasing beds
     * @param numBeds number of beds being released
     * @return whether the beds were released
     */
    public static boolean releaseBeds(int numBeds) {
        return releaseBeds(numBeds, getCurrentKey());
    }

    /**
     * Sets a current shelter variable.
     * @param newShelter The new current shelter.
     */
    public static void setCurrentShelter(Shelter newShelter) {
        currentShelter = newShelter;
    }

    /**
     * Sets a current shelter variable.
     * @param position The position of the new current shelter in the shelter database
     */
    public static void setCurrentShelter(int position) {
        currentShelter = shelterDatabase.get(position);
    }

// --Commented out by Inspection START (4/8/2018 15:18):
//    /**
//     * Returns the current shelter.
//     * @return The current shelter.
//     */
//    public static Shelter getCurrentShelter() {
//        return currentShelter;
//    }
// --Commented out by Inspection STOP (4/8/2018 15:18)

    /**
     * Sets the mapRestrictions variable (used for checking which shelters belong on map)
     * @param mRestrictions The new mapRestrictions.
     */
    public static void setMapRestrictions(Restrictions mRestrictions) {
        mapRestrictions = mRestrictions;
    }

    /**
     * Gets the map restrictions (used for checking shelters on map)
     * @return The map restrictions.
     */
    public static Restrictions getMapRestrictions() {
        return mapRestrictions;
    }

    /**
     * This function looks through the shelterList for a provided name.
     * @param name The name being searched for.
     * @return The shelter with that name.
     */
    public static int findIdByName(String name) {
        for (Shelter shelter : shelterDatabase) {
            if (name.equals(shelter.getName())) {
                return shelter.getUniqueKey();
            }
        }
        return -1;
    }

    /**
     * Finds whether two Restrictions have a match
     * @param r1 First Restrictions
     * @param r2 Second Restrictions
     * @return Whether there was a match
     */
    public static boolean hasMatch(Restrictions r1, Restrictions r2) {
        return r1.hasMatch(r2);
    }

    /**
     * Gets the key of the current shelter.
     * @return The currentShelter key
     */
    private static int getCurrentKey() {
        return currentShelter.getUniqueKey();
    }

    /**
     * Gets the total capacity of the current shelter.
     * @return The currentShelter total capacity.
     */
    public static int getCurrentTotalCapacity() {
        return currentShelter.getTotalCapacity();
    }

    /**
     * Updates failureString from outside class.
     * @param moreFailure What will be appended.
     */
    public static void updateFailureString(String moreFailure) {
        failureString += moreFailure;
    }

    /**
     * Finds a shelter name by a given key.
     * @param uniqueKey The key
     * @return The name
     */
    public static String getNameByID(int uniqueKey) {
        Shelter shelter = shelterDatabase.get(uniqueKey);
        return shelter.getName();
    }
}
