package com.example.androidretrofitexample.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserLog.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "GOOGLE_BOOKS_API_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";

    private static AppDatabase db;

    // builds the database and handles table and DAO changes
    public static synchronized AppDatabase getDatabase(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return db;
    }

    // singleton instance
    public abstract UserLogDAO userLogDAO();
}
