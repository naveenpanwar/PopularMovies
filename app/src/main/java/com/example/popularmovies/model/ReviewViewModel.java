package com.example.popularmovies.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.popularmovies.database.MovieDatabase;

import java.util.List;

public class ReviewViewModel extends AndroidViewModel {
    private MovieDatabase movieDatabase;
    private static final String LOG = ReviewViewModel.class.getSimpleName();

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        movieDatabase = MovieDatabase.getInstance(getApplication());
        Log.d(LOG, "CONSTRUCTOR CALLED DB instancegot");
    }

    public LiveData<List<Review>> getReviews(int id) {
        return movieDatabase.reviewDao().loadReviews(id);
    }
}
