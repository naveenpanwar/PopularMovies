package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    ImageView mMoviePoster;
    TextView mTitleTextView;
    TextView mPlotTextView;
    TextView mRatingTextView;
    TextView mDateTextView;

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

        String poster = "";
        String title = "";
        String plot = "";
        String rating = "";
        String release_date = "";


        if( parentIntent.hasExtra("id")) {
            poster = parentIntent.getStringExtra("poster");
            title = parentIntent.getStringExtra("title");
            plot = parentIntent.getStringExtra("plot");
            rating = parentIntent.getStringExtra("rating");
            release_date = parentIntent.getStringExtra("release_date");

            setTitle("Movie Details");
            mTitleTextView.setText(title);
            mPlotTextView.setText(plot);
            mRatingTextView.setText("Rating: "+rating);
            mDateTextView.setText("Release Date: "+release_date);

            Picasso.get().load(NetworkUtils.getImageURL(poster)).fit().centerCrop().into(mMoviePoster);
        }
    }
}
