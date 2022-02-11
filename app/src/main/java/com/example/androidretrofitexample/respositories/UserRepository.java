/**
 * Author: Pedro Gutierrez Jr.
 * Last Modified: 02/10/2021
 * Abstract: This class is used our activities to pull data from the database
 */

package com.example.androidretrofitexample.respositories;

import android.content.Context;

import com.example.androidretrofitexample.R;
import com.example.androidretrofitexample.room.AppDatabase;
import com.example.androidretrofitexample.room.UserLog;
import com.example.androidretrofitexample.util.SampleUsers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserRepository {
    public static UserRepository instance;

    public List<UserLog> mUsers;
    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context);
        }
        return instance;
    }

    private UserRepository(Context context) {
        mDb = AppDatabase.getDatabase(context);
        mUsers = getAllUsers();
    }

    public void addSampleUsers() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.userLogDAO().insertUsers(SampleUsers.getUsers());
            }
        });
    }

    public List<UserLog> getAllUsers() {
        return mDb.userLogDAO().getAllUsers();

    }

    public UserLog getUserById(int userId) {
        return mDb.userLogDAO().getUserById(userId);
    }

    public UserLog getUserByUsername(String username) {
        return mDb.userLogDAO().getUserByUsername(username);
    }

    public void insertUser(UserLog user) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.userLogDAO().insertUser(user);
            }
        });
    }

    public void insertUsers(List<UserLog> users) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.userLogDAO().insertUsers(users);
            }
        });
    }

    public void deleteUser(final UserLog user) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.userLogDAO().deleteUser(user);
            }
        });
    }
}
