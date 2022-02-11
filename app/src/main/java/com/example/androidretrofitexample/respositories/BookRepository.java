package com.example.androidretrofitexample.respositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidretrofitexample.apis.BookSearchService;
import com.example.androidretrofitexample.models.VolumeResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookRepository {
    private static final String BOOK_SEARCH_SERVICE_BASE_URL = "https://www.googleapis.com/";

    private final BookSearchService bookSearchService;
    private final MutableLiveData<VolumeResponse> volumesResponseLiveData;

    public BookRepository() {
        volumesResponseLiveData = new MutableLiveData<>();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        bookSearchService = new retrofit2.Retrofit.Builder()
                .baseUrl(BOOK_SEARCH_SERVICE_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BookSearchService.class);
    }

    // searches the API for books
    public void searchVolumes(String keyword, String author, String apiKey) {
        bookSearchService.searchVolumes(keyword, author, apiKey)
                .enqueue(new Callback<VolumeResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<VolumeResponse> call, @NonNull Response<VolumeResponse> response) {
                        if (response.body() != null) {
                            volumesResponseLiveData.postValue(response.body());
                            // Toast toast = Toast.makeText(BookRepository.this, "hello.", Toast.LENGTH_SHORT);
                            // toast.show();
                            Log.i("hello", "hello");                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<VolumeResponse> call, @NonNull Throwable t) {
                        volumesResponseLiveData.postValue(null);
                        Log.i("goodbye", "goodbye");
                    }
                });
    }

    public LiveData<VolumeResponse> getVolumesResponseLiveData() {
        return volumesResponseLiveData;
    }
}
