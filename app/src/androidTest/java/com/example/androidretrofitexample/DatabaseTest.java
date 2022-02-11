/**
 * Author: Pedro Gutierrez Jr.
 * Last Modified: 02/10/2021
 * Abstract: Instrumented tests for database functionality
 */

package com.example.androidretrofitexample;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.androidretrofitexample.room.AppDatabase;
import com.example.androidretrofitexample.room.UserLog;
import com.example.androidretrofitexample.room.UserLogDAO;
import com.example.androidretrofitexample.util.SampleUsers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    public static final String TAG = "Junit";
    private AppDatabase mDb;
    private UserLogDAO mDao;

    // creates non-persistent instance of the database
    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();

        mDao = mDb.userLogDAO();

        Log.i(TAG, "created DB");
    }

    // closes the database
    @After
    public void closeDb() {
        mDb.close();

        Log.i(TAG, "closeDb: ");
    }

    // tests for correct insertion of users into the database
    @Test
    public void compareInfo() {
        mDao.insertUsers(SampleUsers.getUsers());
        UserLog original = SampleUsers.getUsers().get(0);
        UserLog fromDB = mDao.getUserById(1);

        assertEquals(original.getUsername(), fromDB.getUsername());
        assertEquals(original.getPassword(), fromDB.getPassword());
        assertEquals(1, fromDB.getUserId());
    }

    // tests creation and retrieval of users from the database
    @Test
    public void createAndRetrieveUsers() {
        mDao.insertUsers(SampleUsers.getUsers());
        int count = mDao.getAllUsers().size();
        assertEquals(SampleUsers.getUsers().size(), count);
    }
}
