package com.example.popularmovies.network;

import android.content.Context;
import android.util.Log;

import com.example.popularmovies.MovieExecutors;
import com.example.popularmovies.database.MovieDatabase;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.utilities.JSONUtils;
import com.example.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class FetchReviewsFromNetwork {
    private static MovieDatabase mMovieDatabase;

    public static void getReviews(Context context, int id) {

        mMovieDatabase = MovieDatabase.getInstance(context);

        final URL url = NetworkUtils.buildReviewsUrl(id);
        MovieExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String reviewsResults = null;

                try {
                    reviewsResults = NetworkUtils.fetchMovieDataFromHttp(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if( reviewsResults != null ) {
                        List<Review> reviews = JSONUtils.getReviewListFromJSON(reviewsResults);
                        for (int i = 0; i < reviews.size(); i++) {
                            Review review = reviews.get(i);
                            Review dbReview = mMovieDatabase.reviewDao().getReviewByID(review.getId());
                            if (dbReview != null) {
                                mMovieDatabase.reviewDao().updateReview(review);
                            } else {
                                mMovieDatabase.reviewDao().insertReview(review);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
