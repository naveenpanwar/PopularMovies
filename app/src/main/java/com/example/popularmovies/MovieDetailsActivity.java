package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieViewModel;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.ReviewViewModel;
import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.model.TrailerViewModel;
import com.example.popularmovies.network.FetchReviewsFromNetwork;
import com.example.popularmovies.network.FetchTrailersFromNetwork;
import com.example.popularmovies.utilities.JSONUtils;
import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements TrailersAdapter.TrailerItemClickListener {
    private final static String MAKE_FAVORITE = "make favorite";
    private final static String REMOVE_FAVORITE = "remove favorite";

    // Views
    private ImageView mMoviePoster;
    private TextView mTitleTextView;
    private TextView mPlotTextView;
    private TextView mRatingTextView;
    private TextView mDateTextView;

    private RecyclerView mReviewsRecyclerView;
    private ReviewsAdapter mReviewsAdapter;

    private RecyclerView mTrailersRecyclerView;
    private TrailersAdapter mTrailersAdapter;

    // Favorite Button
    private Button mFavoriteButton;
    private ImageView mFavoriteImageView;

    // Database Stuff
    private MovieDatabase mMovieDatabase;
    private int mMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieViewModel movieViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
                getApplication()).create(MovieViewModel.class);
        setContentView(R.layout.activity_movie_details);

        mMoviePoster = findViewById(R.id.iv_detail_movie_poster);
        mTitleTextView = findViewById(R.id.tv_movie_title);
        mPlotTextView = findViewById(R.id.tv_movie_plot);
        mRatingTextView = findViewById(R.id.tv_movie_rating);
        mDateTextView = findViewById(R.id.tv_movie_release_date);
        mFavoriteButton = findViewById(R.id.bt_favorite);
        mFavoriteImageView = findViewById(R.id.iv_favorite);

        mMovieDatabase = MovieDatabase.getInstance(getApplicationContext());

        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra("id")) {
            mMovieId = Integer.parseInt(parentIntent.getStringExtra("id"));

            loadReviewsFromNetwork(mMovieId);
            getReviews(mMovieId);
            loadTrailersFromNetwork(mMovieId);
            getTrailers(mMovieId);
        } else {
            Log.d("MOVIE DETAILS", "CANT GET ID");
        }

        movieViewModel.getMovie(mMovieId).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                populateUI(movie);
                mFavoriteButton.setText(movie.getFavorite() ? REMOVE_FAVORITE : MAKE_FAVORITE);
                mFavoriteImageView.setImageResource(movie.getFavorite() ? R.drawable.ic_thumb_down : R.drawable.ic_thumb_up);
            }
        });

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final Movie movie = mMovieDatabase.movieDao().getMovieByIdSimple(mMovieId);
                        movie.setFavorite(!movie.getFavorite());
                        mMovieDatabase.movieDao().updateMovie(movie);
                    }
                });
            }
        });

        // Preparing Reviews RecyclerView
        mReviewsRecyclerView = findViewById(R.id.rv_reviews_list);

        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
        mReviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        mReviewsRecyclerView.setHasFixedSize(true);

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

        mTrailersAdapter = new TrailersAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
    }

    private void populateUI(Movie movie) {
        setTitle("Movie Details");
        mTitleTextView.setText(movie.getOriginalTitle());
        mPlotTextView.setText(movie.getPlot());
        mRatingTextView.setText(String.format(getString(R.string.movie_rating), movie.getRating()));
        mDateTextView.setText(String.format(getString(R.string.movie_release_date), movie.getReleaseDate()));

        mFavoriteButton.setText(movie.getFavorite() ? REMOVE_FAVORITE : MAKE_FAVORITE);
        mFavoriteImageView.setImageResource(movie.getFavorite() ? R.drawable.ic_thumb_down : R.drawable.ic_thumb_up);

        Picasso.get().load(NetworkUtils.getImageURL(movie.getPoster())).fit().centerCrop().into(mMoviePoster);
    }

    private void loadReviewsFromNetwork(int id) {
        FetchReviewsFromNetwork.getReviews(getApplicationContext(), id);
    }

    private void loadTrailersFromNetwork(int id) {
        FetchTrailersFromNetwork.getTrailers(getApplicationContext(), id);
    }

    private void getReviews(int id) {
        ReviewViewModel reviewViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
                getApplication()).create(ReviewViewModel.class);
        reviewViewModel.getReviews(id).observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                if (reviews != null) {
                    mReviewsAdapter.setReviewList(reviews);
                    mReviewsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getTrailers(int id) {
        TrailerViewModel trailerViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
                getApplication()).create(TrailerViewModel.class);
        trailerViewModel.getTrailers(id).observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                if (trailers != null) {
                    mTrailersAdapter.setTrailerList(trailers);
                    mTrailersAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void onTrailerItemClick(String key) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(NetworkUtils.getYouTubeURL(key))
        );
        startActivity(intent);
    }
}
