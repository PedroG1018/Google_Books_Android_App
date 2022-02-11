/**
 * Author: Pedro Gutierrez Jr.
 * Last Modified: 02/10/2021
 * Abstract: Static method in this class can be called to create predefined users if needed
 */

package com.example.androidretrofitexample.util;

import com.example.androidretrofitexample.room.UserLog;

import java.util.ArrayList;
import java.util.List;

public class SampleUsers {
    private static final UserLog USER_1 = new UserLog("Doomslayer", "ripandtear");
    private static final UserLog USER_2 = new UserLog("Kratos", "boy");

    public static List<UserLog> getUsers() {
        List<UserLog> users = new ArrayList<>();

        users.add(USER_1);
        users.add(USER_2);

        return users;
    }
}
