package com.example.popularmovies.network;

import android.content.Context;

import com.example.popularmovies.MovieExecutors;
import com.example.popularmovies.database.MovieDatabase;
import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.utilities.JSONUtils;
import com.example.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class FetchTrailersFromNetwork {
    private static MovieDatabase mMovieDatabase;

    public static void getTrailers(Context context, int id) {
        mMovieDatabase = MovieDatabase.getInstance(context);
        final URL url = NetworkUtils.buildTrailersUrl(id);

        MovieExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String trailersResults = null;
                try {
                    trailersResults = NetworkUtils.fetchMovieDataFromHttp(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if ( trailersResults != null) {
                        List<Trailer> trailers = JSONUtils.getTrailersListFromJSON(trailersResults);
                        for (int i = 0; i < trailers.size(); i++) {
                            Trailer trailer = trailers.get(i);
                            Trailer dbTrailer = mMovieDatabase.trailerDao().getTrailerByID(trailer.getId());
                            if (dbTrailer != null) {
                                mMovieDatabase.trailerDao().updateTrailer(trailer);
                            } else {
                                mMovieDatabase.trailerDao().insertTrailer(trailer);
                            }
                        }
                    }
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
