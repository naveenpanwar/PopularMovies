package com.example.popularmovies.network;

import android.content.Context;

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
    private static URL popularMoviesUrl = NetworkUtils.buildPopularMoviesUrl();
    private static URL topRatedMoviesUrl = NetworkUtils.buildTopRatedMoviesUrl();

    public static void getPopularMovies(Context context) {
        getMovies(popularMoviesUrl, context);
    }

    public static void getTopRatedMovies(Context context) {
        getMovies(topRatedMoviesUrl, context);
    }

    private static void getMovies(final URL url, final Context context) {
        MovieExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                MovieDatabase mDb = MovieDatabase.getInstance(context);

                String popularMoviesResults = null;
                try {
                    popularMoviesResults = NetworkUtils.fetchMovieDataFromHttp(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (popularMoviesResults != null) {
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
        });
    }
}
