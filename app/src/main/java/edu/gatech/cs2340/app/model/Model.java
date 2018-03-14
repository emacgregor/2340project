package edu.gatech.cs2340.app.model;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Database Stuff

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;







public final class Model {
    private static final Model _instance = new Model();
    public static Model getInstance() { return _instance; }
    private static final ArrayList<Shelter> shelterDatabase = new ArrayList<>();
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
     * @param someShelter Shelter to be added.
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
        UserDao userDao = db.userDao();
        userDao.insertAll(nUser);
        setCurrentUser(nUser);
        return true;
    }

    /**
     * Checks to see if the username was registered.
     * @param username The username we're looking for
     * @return Whether this username is registered.
     */
    public boolean userExists(String username, AppDatabase db) {
        UserDao userDao = db.userDao();
        List<String> userNames = userDao.getAllUsername();
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
        UserDao userDao = db.userDao();
        List<User> users = userDao.getAllUsers();
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

    /* This is the old CSV reading method.

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
    }*/

    private static class loadingASyncTask extends AsyncTask<Integer, Void, Void> {
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
                            object.getString("restrictions"),
                            object.getDouble("longit"), object.getDouble("lat"),
                            object.getString("address"),
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
        loadingASyncTask myTask = new loadingASyncTask();
        myTask.execute();
        readSDFile = true;
    }

    private static class updateAsyncTask extends AsyncTask<Integer, Void, Void> {
        final Map<Integer, Integer> userMap;
        final String url_update;
        updateAsyncTask(String url_update, Map<Integer, Integer> map) {
            userMap = map;
            this.url_update = url_update;
        }

        /**
         * Saving product
         */
        @Override
        protected Void doInBackground(Integer... integers) {

            for (Map.Entry<Integer, Integer> entry : userMap.entrySet()) {
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
    }

    public void updateShelterBeds(AppDatabase db) {
        final String url_update = "http://crossoutcancer.org/updateShelter.php";
        UserDao userDao = db.userDao();
        List<User> userList = userDao.getAllUsers();
        Map<Integer, Integer> map = new HashMap<>();

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

        updateAsyncTask updateTask = new updateAsyncTask(url_update, map);
        updateTask.execute();
    }
    private static class updateAsyncTask1 extends AsyncTask<Integer, Void, Void> {
        final String url_update;
        final int shelterID;
        updateAsyncTask1(String url_update, int shelterID) {
            this.url_update = url_update;
            this.shelterID = shelterID;
        }

        /**
         * Saving product
         */
        @Override
        protected Void doInBackground(Integer... integers) {
                    /*int sid = entry.getKey();
                    int beds = entry.getValue();*/
            int sid = shelterID;
            Shelter updateShelter = shelterDatabase.get(shelterID);
            int beds = updateShelter.getRemainingCapacity();
            Log.d("" + shelterID, "" + beds);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url_update).newBuilder();
            urlBuilder.addQueryParameter("id", String.valueOf(sid));
            urlBuilder.addQueryParameter("remainingCap", String.valueOf(beds));
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
            return null;
        }
    }

    private void updateCurrentShelterBeds(final int shelterID) {
        final String url_update = "http://crossoutcancer.org/updateShelter.php";
        /*List<User> userList = db.userDao().getAllUsers();
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

        final Map<Integer, Integer> usermap = map;*/

        updateAsyncTask1 updateTask = new updateAsyncTask1(url_update, shelterID);
        updateTask.execute();
    }
    private void setCurrentUser(User user) {
        currentUser = user;
    }
    public User getCurrentUser() {
        return  currentUser;
    }
    public boolean claimBeds(int numBeds, int shelterID) {
        Shelter updateShelter = shelterDatabase.get(shelterID);
        if (currentUser.canClaimBeds(shelterID)
                && updateShelter.canClaimBeds(numBeds)) {
            currentUser.claimBeds(numBeds, shelterID);
            updateShelter.claimBeds(numBeds);
            updateCurrentShelterBeds(shelterID);
            return true;
        } else {
            failureString = "You cannot claim these beds.";
            if (!currentUser.canClaimBeds(shelterID)) {
                Shelter userShelter = shelterDatabase.get(currentUser.getShelterID());
                failureString += " You already own beds at " + userShelter.getName() + ".";
            }
            if (!updateShelter.canClaimBeds(numBeds)) {
                failureString += " This shelter does not have that many beds to spare.";
            }
            return false;
        }
    }
    public String getFailureString() {
        return failureString;
    }
    public boolean releaseBeds(int numBeds, int shelterID) {
        Shelter updateShelter = shelterDatabase.get(shelterID);
        if (currentUser.canReleaseBeds(numBeds, shelterID)
                && updateShelter.canReleaseBeds(numBeds)) {
            currentUser.releaseBeds(numBeds, shelterID);
            updateShelter.releaseBeds(numBeds);
            updateCurrentShelterBeds(shelterID);
            return true;
        } else {
            failureString = "You cannot release beds at this shelter:";
            if (currentUser.getShelterID() == -1) {
                failureString += " You do not own any beds.";
            } else if (currentUser.getShelterID() != shelterID) {
                Shelter userShelter = shelterDatabase.get(currentUser.getShelterID());
                failureString += " Your beds are from " + userShelter.getName() + ".";
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
