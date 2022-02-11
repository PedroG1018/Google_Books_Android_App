/**
 * Author: Pedro Gutierrez Jr.
 * Last Modified: 02/10/2021
 * Abstract: Unit tests for UserLog.java
 */

package com.example.androidretrofitexample;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.androidretrofitexample.room.UserLog;

public class UserTest {
    private UserLog user = new UserLog("testuser", "testuser");

    // test the getUserId method
    @Test
    public void getUserIdTest() {
        assertEquals(user.getUserId(), 0);
    }

    // tests the setUserId method
    @Test
    public void setUserIdTest() {
        assertEquals(user.getUserId(), 0);

        user.setUserId(1);

        assertEquals(user.getUserId(), 1);
    }

    // tests the getUsername method
    @Test
    public void getUsernameTest() {
        assertEquals(user.getUsername(), "testuser");
    }

    // tests the setUsername method
    @Test
    public void setUsernameTest() {
        assertEquals(user.getUsername(), "testuser");

        user.setUsername("usertest");

        assertEquals(user.getUsername(), "usertest");
    }

    // tests the getPassword method
    @Test
    public void getPasswordTest() {
        assertEquals(user.getPassword(), "testuser");
    }

    // tests the setPassword method
    @Test
    public void setPasswordTest() {
        assertEquals(user.getPassword(), "testuser");

        user.setPassword("usertest");

        assertEquals(user.getPassword(), "usertest");
    }
}
