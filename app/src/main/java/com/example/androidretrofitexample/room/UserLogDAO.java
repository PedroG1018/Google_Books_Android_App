/**
 * Author: Pedro Gutierrez Jr.
 * Last Modified: 02/10/2021
 * Abstract: User Data Access Object
 */

package com.example.androidretrofitexample.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserLogDAO {
    // inserts a user
    // doubles as an update method
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserLog user);

    // inserts a list of users
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<UserLog> users);

    // deletes a user
    @Delete
    void deleteUser(UserLog user);

    // retrieves a user with matching ID
    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE userId = :userId")
    UserLog getUserById(int userId);

    // retrieves a user with matching username
    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE username = :username")
    UserLog getUserByUsername(String username);

    // retrieves all users in the database
    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " ORDER BY username DESC")
    List<UserLog> getAllUsers();
}
