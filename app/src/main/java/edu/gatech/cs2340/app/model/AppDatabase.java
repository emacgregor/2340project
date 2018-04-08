package edu.gatech.cs2340.app.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * This is the database that holds all the users.
 */
@SuppressWarnings("LawOfDemeter")
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    @Nullable
    private static AppDatabase INSTANCE;

    /**
     * Returns UserDao.
     * @return The user finder, basically. See UserDao for more info.
     */
    public abstract UserDao userDao();

    /**
     * Returns the app's database
     * @param context The context of the app as a whole.
     * @return The app's database.
     */
    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            Builder<AppDatabase> instanceBuilder = Room.databaseBuilder(
                    context.getApplicationContext(), AppDatabase.class, "user-database");
            INSTANCE = instanceBuilder.build();
        }
        return INSTANCE;
    }

// --Commented out by Inspection START (4/7/2018 16:56):
//    /**
//     * Destroys the instance of AppDatabase
//     */
//    public static void destroyInstance() {
//        INSTANCE = null;
//    }
// --Commented out by Inspection STOP (4/7/2018 16:56)
    void insertAll(User nUser) {
        UserDao userDao = userDao();
        userDao.insertAll(nUser);
    }
    List<String> getAllUsername() {
        UserDao userDao = userDao();
        return userDao.getAllUsername();
    }
    List<User> getAllUsers() {
        UserDao userDao = userDao();
        return  userDao.getAllUsers();
    }
}