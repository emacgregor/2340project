package edu.gatech.cs2340.app.model;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * This will hold all logic to do with shelters.
 */

@SuppressWarnings({"UtilityClass", "CyclicClassDependency", "ClassWithTooManyDependents"})
public class ShelterDatabase {
    private static boolean readSDFile = false;
    private static final ArrayList<Shelter> shelterDatabase = new ArrayList<>();
    private static Shelter currentShelter;
    private static String failureString = "";

    /**
     * Adds a shelter to an the array list of shelters
     * @param someShelter This shelter gets added to the shelter database.
     */
    public static void addShelter(Shelter someShelter) {
        shelterDatabase.add(someShelter);
        /*dbUpdateTask dbUpdater = new dbUpdateTask(someShelter.getUniqueKey());
        dbUpdater.execute();*/

        //Add new shelter here.
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
     * Clears the database.
     */
    private static void clear() {
        shelterDatabase.clear();
    }

// --Commented out by Inspection START (4/8/2018 17:23):
//    /**
//     * Gets from the shelterDatabase at the correct index.
//     * @param index The index
//     * @return The shelter
//     */
//    private static Shelter get(int index) {
//        return shelterDatabase.get(index);
//    }
// --Commented out by Inspection STOP (4/8/2018 17:23)
    /**
     * Provides all the logic for claiming beds with the current user and the provided shelter.
     * @param numBeds The number of beds that are being claimed.
     * @param shelterID The shelter that beds are being claimed in.
     * @return Whether the beds were claimed.
     */
    private static boolean claimBeds(int numBeds, int shelterID) {
        failureString = "You cannot claim these beds.";
        Shelter shelter = shelterDatabase.get(shelterID);
        if (Model.currentUserCanClaimBeds(shelterID)
                && shelter.canClaimBeds(numBeds)) {
            Model.currentUserClaimBeds(numBeds, shelterID);
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
        if (Model.currentUserCanReleaseBeds(numBeds, shelterID)
                && shelter.canReleaseBeds(numBeds)) {
            Model.currentUserReleaseBeds(numBeds, shelterID);
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
        if ((uniqueKey < shelterDatabase.size()) && (uniqueKey > -1)) {
            Shelter shelter = shelterDatabase.get(uniqueKey);
            return shelter.getName();
        } else {
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
                ShelterDatabase.clear();

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
                                    object.getString("phoneNumber"),
                                    object.getString("specialNotes"),
                                    object.getString("restrictions")));

                    ShelterDatabase.addShelter(shelter);
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
     * This exists solely for testing purposes
     */
    public static void clearFailureString() {
        failureString = "";
    }
    public static int getNewUniqueKey() {
        return shelterDatabase.size();
    }
}
