package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.utilities.JSONUtils;
import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {
    private ImageView mMoviePoster;
    private TextView mTitleTextView;
    private TextView mPlotTextView;
    private TextView mRatingTextView;
    private TextView mDateTextView;

    // List of Reviews
    private List<Review> mReviewList;

    private RecyclerView mReviewsRecyclerView;
    private ReviewsAdapter mReviewsAdapter;

    // List of Trailers
    private List<Trailer> mTrailersList;

    private RecyclerView mTrailersRecyclerView;
    private TrailersAdapter mTrailersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mMoviePoster = findViewById(R.id.iv_detail_movie_poster);
        mTitleTextView = findViewById(R.id.tv_movie_title);
        mPlotTextView = findViewById(R.id.tv_movie_plot);
        mRatingTextView = findViewById(R.id.tv_movie_rating);
        mDateTextView = findViewById(R.id.tv_movie_release_date);

        // Preparing Reviews RecyclerView
        mReviewsRecyclerView = findViewById(R.id.rv_reviews_list);

        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
        mReviewsRecyclerView.setLayoutManager(reviewsLayoutManager);

        mReviewsRecyclerView.setHasFixedSize(true);

        if( mReviewList != null ) {
            Log.d("INITIAL REVIEWS LIST", "" + mReviewList.size()+"YES");
        }
        else {
            Log.d("INITIAL REVIEWS LIST", "" + 0+"NO");
        }
        mReviewsAdapter =  new ReviewsAdapter();
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        // Preparing Trailers RecyclerView
        mTrailersRecyclerView = findViewById(R.id.rv_trailers);

        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(this);
        mTrailersRecyclerView.setLayoutManager(trailersLayoutManager);

        mTrailersRecyclerView.setHasFixedSize(true);

        if( mTrailersList != null ) {
            Log.d("INITIAL TRAILER LIST", "" + mTrailersList.size()+"YES");
        }
        else {
            Log.d("INITIAL TRAILER LIST", "" + 0+"NO");
        }

        mTrailersAdapter = new TrailersAdapter();
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);

        Intent parentIntent = getIntent();

        if( parentIntent.hasExtra("id")) {
            String movieID = parentIntent.getStringExtra("id");
            String poster = parentIntent.getStringExtra("poster");
            String title = parentIntent.getStringExtra("title");
            String plot = parentIntent.getStringExtra("plot");
            String rating = parentIntent.getStringExtra("rating");
            String release_date = parentIntent.getStringExtra("release_date");

            setTitle("Movie Details");
            mTitleTextView.setText(title);
            mPlotTextView.setText(plot);
            mRatingTextView.setText(String.format(getString(R.string.movie_rating),rating));
            mDateTextView.setText(String.format(getString(R.string.movie_release_date),release_date));

            Picasso.get().load(NetworkUtils.getImageURL(poster)).fit().centerCrop().into(mMoviePoster);

            getReviews(movieID);
            getTrailers(movieID);
        }
    }

    private void getReviews(String id) {
        URL url = NetworkUtils.buildReviewsUrl(id);
        new ReviewsAsyncTask(this).execute(url);
    }

    private void getTrailers(String id) {
        URL url = NetworkUtils.buildTrailersUrl(id);
        new TrailersAsyncTask(this).execute(url);
    }

    class ReviewsAsyncTask extends AsyncTask<URL,Void, String> {
        final private Context mContext;

        ReviewsAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String reviewsResults = null;
            try {
                reviewsResults = NetworkUtils.fetchMovieDataFromHttp(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return reviewsResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if ( s != null && !s.equals("")) {
                try {
                    mReviewList = JSONUtils.getReviewListFromJSON(s);
                    mReviewsAdapter.setReviewList(mReviewList);
                    mReviewsAdapter.notifyDataSetChanged();
                    Log.d("UPDATED REVIEWS LIST",""+mReviewList.size());
                } catch (JSONException e) {
                    mReviewList = null;
                    e.printStackTrace();
                }
            }
        }
    }

    class TrailersAsyncTask extends AsyncTask<URL,Void, String> {
        final private Context mContext;

        TrailersAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String trailersResults = null;
            try {
                trailersResults = NetworkUtils.fetchMovieDataFromHttp(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return trailersResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if ( s != null && !s.equals("")) {
                try {
                    mTrailersList = JSONUtils.getTrailersListFromJSON(s);
                    mTrailersAdapter.setTrailerList(mTrailersList);
                    mTrailersAdapter.notifyDataSetChanged();
                    Log.d("UPDATED TRAILERS LIST",""+mTrailersList.size());
                } catch (JSONException e) {
                    mTrailersList = null;
                    e.printStackTrace();
                }
            }
        }
    }
}
