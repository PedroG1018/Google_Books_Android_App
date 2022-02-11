/**
 * Author: Pedro Gutierrez Jr.
 * Last Modified: 02/09/2022
 * Abstract: Login activity to allow users to log in to the app
 *
 * Predefined User Info:
 *      User 1:
 *          Username = 'Doomslayer', Password = 'ripandtear'
 *      User 2:
 *          Username = 'Kratos', Password = 'boy'
 */

package com.example.androidretrofitexample.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidretrofitexample.R;
import com.example.androidretrofitexample.respositories.UserRepository;
import com.example.androidretrofitexample.room.AppDatabase;
import com.example.androidretrofitexample.room.UserLog;
import com.example.androidretrofitexample.room.UserLogDAO;
import com.example.androidretrofitexample.util.Util;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameField;
    private EditText mPasswordField;

    private Button mLoginBtn;

    private String mUsername;
    private String mPassword;

    private UserLog mUser;
    private UserRepository mRepository;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wireUpDisplay();
    }

    // wires up all display elements and on click listeners
    private void wireUpDisplay() {
        mUsernameField = findViewById(R.id.username_edittext);
        mPasswordField = findViewById(R.id.password_edittext);

        mLoginBtn = findViewById(R.id.login_btn);

        mSharedPreferences = getSharedPreferences(Util.PREFERENCES_KEY, MODE_PRIVATE);

        // will automatically log user into their account if they are still saved in shared preferences
        if (mSharedPreferences.getInt(Util.USER_ID_KEY, -1) != -1) {
            Intent intent = MainActivity.newIntent(getApplicationContext(), mSharedPreferences.getInt(Util.USER_ID_KEY, -1));
            startActivity(intent);
        }

        mRepository = UserRepository.getInstance(getApplicationContext());
        sampleUsers();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();

                if (checkForUserInDatabase()) {
                    if (!validatePassword()) {
                        Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    } else {
                        saveUser();

                        Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
                        Intent intent = MainActivity.newIntent(getApplicationContext(), mUser.getUserId());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    // creates predefined users
    public void sampleUsers() {
        if (mRepository.getAllUsers().size() == 0) {
            mRepository.addSampleUsers();
        }
    }

    // retrieve values from username and password fields
    public void getValuesFromDisplay() {
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    // checks if user has an account
    public boolean checkForUserInDatabase() {
        mUser = mRepository.getUserByUsername(mUsername);

        if (mUser == null) {
            Toast.makeText(LoginActivity.this, "Invalid Username", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // validates entered password
    public boolean validatePassword() {
        return mUser.getPassword().equals(mPassword);
    }

    // saves user to shared preferences
    public void saveUser() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(Util.USER_ID_KEY, mUser.getUserId());
        editor.apply();
    }

    // factory intent to swtich activities
    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, LoginActivity.class);
        return intent;
    }
}