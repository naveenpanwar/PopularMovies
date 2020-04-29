package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.database.MovieDatabase;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.utilities.JSONUtils;
import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements TrailersAdapter.TrailerItemClickListener {
    private final static String FAVORITE_IT = "favorite it";
    private final static String UNFAVORITE_IT = "unfavorite it";
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

    private Button mFavoriteButton;
    private ImageView mFavoriteImageView;

    private MovieDatabase mMovieDatabase;
    private boolean mFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mMoviePoster = findViewById(R.id.iv_detail_movie_poster);
        mTitleTextView = findViewById(R.id.tv_movie_title);
        mPlotTextView = findViewById(R.id.tv_movie_plot);
        mRatingTextView = findViewById(R.id.tv_movie_rating);
        mDateTextView = findViewById(R.id.tv_movie_release_date);
        mFavoriteButton = findViewById(R.id.bt_favorite);
        mFavoriteImageView = findViewById(R.id.iv_favorite);

        mMovieDatabase = MovieDatabase.getInstance(getApplicationContext());

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mFavorite ) {
                    mFavorite = false;
                    mFavoriteButton.setText(UNFAVORITE_IT);
                    mFavoriteImageView.setImageResource(R.drawable.ic_launcher_background);
                } else {
                    mFavorite = true;
                    mFavoriteButton.setText(FAVORITE_IT);
                    mFavoriteImageView.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }
        });

        // Preparing Reviews RecyclerView
        mReviewsRecyclerView = findViewById(R.id.rv_reviews_list);

        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
        mReviewsRecyclerView.setLayoutManager(reviewsLayoutManager);

        mReviewsRecyclerView.setHasFixedSize(true);

        if (mReviewList != null) {
            Log.d("INITIAL REVIEWS LIST", "" + mReviewList.size() + "YES");
        } else {
            Log.d("INITIAL REVIEWS LIST", "" + 0 + "NO");
        }
        mReviewsAdapter = new ReviewsAdapter();
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        // Preparing Trailers RecyclerView
        mTrailersRecyclerView = findViewById(R.id.rv_trailers);

        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                true
        );
        mTrailersRecyclerView.setLayoutManager(trailersLayoutManager);

        mTrailersRecyclerView.setHasFixedSize(true);

        if (mTrailersList != null) {
            Log.d("INITIAL TRAILER LIST", "" + mTrailersList.size() + "YES");
        } else {
            Log.d("INITIAL TRAILER LIST", "" + 0 + "NO");
        }

        mTrailersAdapter = new TrailersAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);

        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra("id")) {
            String movieID = parentIntent.getStringExtra("id");
            String poster = parentIntent.getStringExtra("poster");
            String title = parentIntent.getStringExtra("title");
            String plot = parentIntent.getStringExtra("plot");
            String rating = parentIntent.getStringExtra("rating");
            String release_date = parentIntent.getStringExtra("release_date");

            mFavorite = Boolean.parseBoolean(parentIntent.getStringExtra("favorite"));

            setTitle("Movie Details");
            mTitleTextView.setText(title);
            mPlotTextView.setText(plot);
            mRatingTextView.setText(String.format(getString(R.string.movie_rating), rating));
            mDateTextView.setText(String.format(getString(R.string.movie_release_date), release_date));

            if( mFavorite ) {
                mFavoriteButton.setText(UNFAVORITE_IT);
                mFavoriteImageView.setImageResource(R.drawable.ic_launcher_foreground);

            } else {
                mFavoriteButton.setText(FAVORITE_IT);
                mFavoriteImageView.setImageResource(R.drawable.ic_launcher_background);
            }

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

    @Override
    public void onTrailerItemClick(int clickedTrailerIndex) {
        Trailer trailer = mTrailersList.get(clickedTrailerIndex);
        Log.d("Trailer Name", trailer.getName());
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(NetworkUtils.getYouTubeURL(trailer.getKey()))
        );
        startActivity(intent);
    }

    class ReviewsAsyncTask extends AsyncTask<URL, Void, List<Review>> {
        final private Context mContext;

        ReviewsAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected List<Review> doInBackground(URL... urls) {
            URL url = urls[0];
            String reviewsResults = null;
            try {
                reviewsResults = NetworkUtils.fetchMovieDataFromHttp(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
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
                return reviews;
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            if (reviews != null) {
                mReviewList = reviews;
                mReviewsAdapter.setReviewList(mReviewList);
                mReviewsAdapter.notifyDataSetChanged();
                Log.d("UPDATED REVIEWS LIST", "" + mReviewList.size());
            }
        }
    }

    class TrailersAsyncTask extends AsyncTask<URL, Void, List<Trailer>> {
        final private Context mContext;

        TrailersAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected List<Trailer> doInBackground(URL... urls) {
            URL url = urls[0];
            String trailersResults = null;
            try {
                trailersResults = NetworkUtils.fetchMovieDataFromHttp(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
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

                return trailers;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            if (trailers != null) {
                mTrailersList = trailers;
                mTrailersAdapter.setTrailerList(mTrailersList);
                mTrailersAdapter.notifyDataSetChanged();
                Log.d("UPDATED TRAILERS LIST", "" + mTrailersList.size());
            }
        }
    }
}
