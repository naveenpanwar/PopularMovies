package com.example.popularmovies.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.popularmovies.MovieExecutors;
import com.example.popularmovies.database.MovieDatabase;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utilities.JSONUtils;
import com.example.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

public class FetchMoviesFromNetwork {
    private static Context mContext;

    public FetchMoviesFromNetwork(Context context) {
        mContext = context;
    }

    public void getPopularMovies() {
        URL popularMoviesUrl = NetworkUtils.buildPopularMoviesUrl();
        getMovies(popularMoviesUrl);
    }

    public void getTopRatedMovies() {
        URL topRatedMoviesUrl = NetworkUtils.buildTopRatedMoviesUrl();
        getMovies(topRatedMoviesUrl);
    }

    private void getMovies(URL url) {
        MovieDatabase mDb = MovieDatabase.getInstance(mContext);

        String popularMoviesResults = null;
        try {
            popularMoviesResults = NetworkUtils.fetchMovieDataFromHttp(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            List<Movie> movies = JSONUtils.getMovieListFromJSON(popularMoviesResults);
            for (int i = 0; i < movies.size(); i++) {
                Movie networkMovie = movies.get(i);
                Movie dbMovie = mDb.movieDao().getMovieByIdSimple(networkMovie.getId());
                if (dbMovie != null) {
                    networkMovie.setFavorite(dbMovie.getFavorite());
                    mDb.movieDao().updateMovie(networkMovie);
                } else {
                    mDb.movieDao().insertMovie(networkMovie);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
