/**
 * Author: Pedro Gutierrez Jr.
 * Last Modified: 02/10/2021
 * Abstract: Instrumented tests for MainActivity.java
 */

package com.example.androidretrofitexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.androidretrofitexample.room.AppDatabase;
import com.example.androidretrofitexample.room.UserLog;
import com.example.androidretrofitexample.room.UserLogDAO;
import com.example.androidretrofitexample.util.Util;
import com.example.androidretrofitexample.views.MainActivity;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private AppDatabase mDb;
    private UserLogDAO mDao;
    private SharedPreferences mPrefs;

    // creates non-persistent instance of the database
    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mPrefs = context.getSharedPreferences(Util.PREFERENCES_KEY, Context.MODE_PRIVATE);

        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();

        mDao = mDb.userLogDAO();

        Log.i(DatabaseTest.TAG, "created DB");
    }

    // closes the database
    @After
    public void closeDb() {
        mDb.close();

        Log.i(DatabaseTest.TAG, "closeDb: ");
    }

    // tests logout() method's logic
    @Test
    public void logoutTest() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // clear user from intent part
        mDao.insertUser(new UserLog("testuser", "testuser"));
        UserLog user = mDao.getUserById(1);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Util.USER_ID_KEY, user.getUserId());

        assertEquals(user.getUserId(), intent.getIntExtra(Util.USER_ID_KEY, -1));

        intent.putExtra(Util.USER_ID_KEY, -1);

        assertNotEquals(user.getUserId(), intent.getIntExtra(Util.USER_ID_KEY, -1));
        assertEquals(-1, intent.getIntExtra(Util.USER_ID_KEY, -1));

        // clear user from preferences part
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(Util.USER_ID_KEY, user.getUserId());
        editor.apply();

        assertEquals(user.getUserId(), mPrefs.getInt(Util.USER_ID_KEY, -1));

        editor.putInt(Util.USER_ID_KEY, -1);
        editor.apply();

        assertNotEquals(user.getUserId(), mPrefs.getInt(Util.USER_ID_KEY, -1));
        assertEquals(-1, mPrefs.getInt(Util.USER_ID_KEY, -1));
    }

    // tests creation of our MainActivity intent
    @Test
    public void mainIntentTest() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = null;

        assertNull(intent);

        intent = new Intent(context, MainActivity.class);

        assertNotNull(intent);
    }
}
