package com.example.androidretrofitexample.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.androidretrofitexample.models.VolumeResponse;
import com.example.androidretrofitexample.respositories.BookRepository;

public class BookSearchViewModel extends AndroidViewModel {
    private BookRepository bookRepository;
    private LiveData<VolumeResponse> volumeResponseLiveData;

    public BookSearchViewModel(@NonNull Application application) {
        super(application);
    }

    // initializes the view model
    public void init() {
        bookRepository = new BookRepository();
        volumeResponseLiveData = bookRepository.getVolumesResponseLiveData();
    }

    // searches book volumes based on user inputted keyword and author
    public void searchVolumes(String keyword, String author, String apiKey) {
        bookRepository.searchVolumes(keyword, author, apiKey);
    }

    // gets a live data object containing response from API
    public LiveData<VolumeResponse> getVolumeResponseLiveData() {
        return volumeResponseLiveData;
    }
}
