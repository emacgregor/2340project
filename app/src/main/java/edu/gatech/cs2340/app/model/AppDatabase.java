package edu.gatech.cs2340.app.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * This is the database that holds all the users.
 */
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

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

    /**
     * Destroys the instance of AppDatabase
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }
}