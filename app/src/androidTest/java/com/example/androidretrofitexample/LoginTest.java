/**
 * Author: Pedro Gutierrez Jr.
 * Last Modified: 02/10/2021
 * Abstract: Instrumented tests for LoginActivity.java
 */

package com.example.androidretrofitexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.androidretrofitexample.room.AppDatabase;
import com.example.androidretrofitexample.room.UserLog;
import com.example.androidretrofitexample.room.UserLogDAO;
import com.example.androidretrofitexample.util.SampleUsers;
import com.example.androidretrofitexample.util.Util;
import com.example.androidretrofitexample.views.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class LoginTest {
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

    // tests sampleUsers() method's logic
    @Test
    public void sampleUsersTest() {
        assertEquals(0, mDao.getAllUsers().size());

        mDb.userLogDAO().insertUsers(SampleUsers.getUsers());

        assertEquals(2, mDao.getAllUsers().size());
    }

    // tests checkForUserInDatabase() method's logic
    @Test
    public void checkForUserInDatabaseTest() {
        UserLog user = null;

        assertNull(user);
        assertNull(mDao.getUserByUsername("testuser"));

        mDao.insertUser(new UserLog("testuser", "testuser"));
        user = mDao.getUserByUsername("testuser");

        assertNotNull(user);
    }

    // tests validatePassword method's logic
    @Test
    public void validatePasswordTest() {
        mDao.insertUser(new UserLog("testuser", "testuser"));
        UserLog user = mDao.getUserById(1);

        assertNotNull(user);

        assertFalse(user.getPassword().equals("usertest"));
        assertTrue(user.getPassword().equals("testuser"));
    }

    // tests saveUser() method's logic
    @Test
    public void saveUserTest() {
        assertEquals(-1, mPrefs.getInt(Util.USER_ID_KEY, -1));

        mDao.insertUser(new UserLog("testuser", "testuser"));
        UserLog user = mDao.getUserById(1);

        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(Util.USER_ID_KEY, user.getUserId());
        editor.apply();

        assertEquals(user.getUserId(), mPrefs.getInt(Util.USER_ID_KEY, -1));

        editor.putInt(Util.USER_ID_KEY, -1);
        editor.apply();
    }

    // tests creation of our LoginActivity intent
    @Test
    public void loginIntentTest() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = null;

        assertNull(intent);

        intent = new Intent(context, LoginActivity.class);

        assertNotNull(intent);
    }
}
