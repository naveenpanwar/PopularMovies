package com.example.popularmovies.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.popularmovies.database.MovieDatabase;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> topRatedMovies;
    private LiveData<List<Movie>> popularMovies;
    private LiveData<List<Movie>> favouriteMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        topRatedMovies = movieDatabase.movieDao().loadMoviesByTopRating();
        popularMovies = movieDatabase.movieDao().loadMoviesByPopularity();
        favouriteMovies = movieDatabase.movieDao().loadMoviesByFavorite();
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        return topRatedMovies;
    }

    public LiveData<List<Movie>> getPopularMovies() {
        return popularMovies;
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
        return favouriteMovies;
    }
}
