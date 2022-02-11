/**
 * Author: Pedro Gutierrez Jr.
 * Last Modified: 02/10/2021
 * Abstract: Instrumented tests for UserDAO.java
 */

package com.example.androidretrofitexample;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UserDAO_Test {
    private AppDatabase mDb;
    private UserLogDAO mDao;
    private UserLog user = new UserLog("testuser", "testuser");

    // creates non-persistent instance of the database
    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
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

    // tests functionality of inserting one user
    @Test
    public void insertUserTest() {
        int currentTableSize = mDao.getAllUsers().size();

        mDao.insertUser(user);

        assertEquals(mDao.getAllUsers().size(), currentTableSize + 1);

        assertNotNull(mDao.getUserByUsername("testuser").getUsername());
        assertNotNull(mDao.getUserByUsername("testuser").getPassword());

        user = mDao.getUserByUsername("testuser");
    }

    // tests functionality of inserting multiple users
    @Test
    public void insertUsersTest() {
        assertEquals(0, mDao.getAllUsers().size());

        List<UserLog> users = new ArrayList<>();
        UserLog user1 = new UserLog("testuser1", "testuser1");
        UserLog user2 = new UserLog("testuser2", "testuser2");
        UserLog user3 = new UserLog("testuser3", "testuser3");

        users.add(user1);
        users.add(user2);
        users.add(user3);

        mDao.insertUsers(users);

        assertEquals(3, mDao.getAllUsers().size());
    }

    // tests functionality of updating users
    @Test
    public void updateUserTest() {
        mDao.insertUser(user);

        user = mDao.getUserById(1);

        user.setUsername("otheruser");
        user.setPassword("otheruser");
        mDao.insertUser(user);

        assertEquals(user.getUsername(), mDao.getUserById(1).getUsername());
        assertEquals(user.getPassword(), mDao.getUserById(1).getPassword());
    }

    // tests functionality of deleting users
    @Test
    public void deleteUserTest() {
        int currentTableSize = mDao.getAllUsers().size();

        mDao.insertUser(user);

        assertEquals(mDao.getAllUsers().size(), currentTableSize + 1);

        user = mDao.getUserByUsername("testuser");

        mDao.deleteUser(user);

        assertEquals(mDao.getAllUsers().size(), currentTableSize);
    }

    // tests functionality of getting users by their ID
    @Test
    public void getUserByUserId() {
        assertNull(mDao.getUserById(-2));

        user.setUserId(-2);
        mDao.insertUser(user);

        assertNotNull(mDao.getUserById(-2));

        user = mDao.getUserById(-2);
        assertEquals(user.getUsername(), "testuser");
        assertEquals(user.getPassword(), "testuser");
    }

    // tests functionality of getting users by their username
    @Test
    public void getUserByUsername() {
        assertNull(mDao.getUserByUsername("testuser"));

        mDao.insertUser(user);

        assertNotNull(mDao.getUserByUsername("testuser"));

        user = mDao.getUserByUsername("testuser");
        assertEquals(user.getUsername(), "testuser");
        assertEquals(user.getPassword(), "testuser");
    }

    // tests functionality of getting all current users in the database
    @Test
    public void getAllUsersTest() {
        List<UserLog> users = null;

        assertNotEquals(mDao.getAllUsers(), users);

        UserLog user1 = new UserLog("testuser1", "testuser1");
        UserLog user2 = new UserLog("testuser2", "testuser2");
        UserLog user3 = new UserLog("testuser3", "testuser3");

        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        mDao.insertUsers(users);
        users = mDao.getAllUsers();

        assertEquals(users.size(), mDao.getAllUsers().size());
        assertNotNull(users);
    }
}
