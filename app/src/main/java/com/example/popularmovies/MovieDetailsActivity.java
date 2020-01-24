package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    private ImageView mMoviePoster;
    private TextView mTitleTextView;
    private TextView mPlotTextView;
    private TextView mRatingTextView;
    private TextView mDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mMoviePoster = findViewById(R.id.iv_detail_movie_poster);
        mTitleTextView = findViewById(R.id.tv_movie_title);
        mPlotTextView = findViewById(R.id.tv_movie_plot);
        mRatingTextView = findViewById(R.id.tv_movie_rating);
        mDateTextView = findViewById(R.id.tv_movie_release_date);

        Intent parentIntent = getIntent();

        if( parentIntent.hasExtra("id")) {
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
        }
    }
}
