/**
 * Author: Pedro Gutierrez Jr.
 * Last Modified: 02/10/2022
 * Abstract: Main activity allows user to search for books through the Google Books API
 */

package com.example.androidretrofitexample.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidretrofitexample.ApiKeys;
import com.example.androidretrofitexample.R;
import com.example.androidretrofitexample.adapters.BookSearchResultsAdapter;
import com.example.androidretrofitexample.models.VolumeResponse;
import com.example.androidretrofitexample.respositories.UserRepository;
import com.example.androidretrofitexample.room.UserLog;
import com.example.androidretrofitexample.util.Util;
import com.example.androidretrofitexample.viewmodels.BookSearchViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private BookSearchViewModel viewModel;
    private BookSearchResultsAdapter adapter;

    private TextInputEditText keywordEditText, authorEditText;

    private UserLog mUser;
    private UserRepository mRepository;

    private TextView heading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new BookSearchResultsAdapter();

        // view model for the recycler view
        viewModel = new ViewModelProvider(this).get(BookSearchViewModel.class);
        viewModel.init();
        viewModel.getVolumeResponseLiveData().observe(this, new Observer<VolumeResponse>() {
            @Override
            public void onChanged(VolumeResponse volumeResponse) {
                if (volumeResponse != null) {
                    adapter.setResults(volumeResponse.getItems());
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.booksearch_searchResultsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        heading = findViewById(R.id.search_textview);
        keywordEditText = findViewById(R.id.booksearch_keyword);
        authorEditText = findViewById(R.id.booksearch_author);
        Button searchButton = findViewById(R.id.booksearch_search);

        mRepository = UserRepository.getInstance(getApplicationContext());
        displayName();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    // performs Google Books API search
    private void performSearch() {
        String keyword = keywordEditText.getEditableText().toString();
        String author = authorEditText.getEditableText().toString();

        viewModel.searchVolumes(keyword, author, ApiKeys.API_KEY);
    }

    // displays name of current user
    public void displayName() {
        int userId = getIntent().getIntExtra(Util.USER_ID_KEY, -1);
        mUser = mRepository.getUserById(userId);
        heading.append(": Welcome, " + mUser.getUsername());
    }


    // logout menu icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // logs out the user
    public void logout() {
        getIntent().putExtra(Util.USER_ID_KEY, -1);

        clearUserFromPreference();

        Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
        Intent intent = LoginActivity.newIntent(getApplicationContext());
        startActivity(intent);
    }

    // clears current user from shared preferences
    public void clearUserFromPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(Util.PREFERENCES_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Util.USER_ID_KEY, -1);
        editor.apply();
    }

    // factory intent to swtich activities
    public static Intent newIntent(Context packageContext, int userId) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(Util.USER_ID_KEY, userId);
        return intent;
    }
}