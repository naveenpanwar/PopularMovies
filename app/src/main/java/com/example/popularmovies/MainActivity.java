package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mPopularMoviesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPopularMoviesTextView = findViewById(R.id.tv_movies_data);
        getPopularMovies();
    }

    private void getPopularMovies() {
        URL url = NetworkUtils.buildUrl();
        new PopularMoviesAsyncTask().execute(url);
    }

    public class PopularMoviesAsyncTask extends AsyncTask<URL,Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String popularMoviesResults = null;
            try {
                popularMoviesResults = NetworkUtils.fetchMovieDataFromHttp(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return popularMoviesResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if ( s != null && !s.equals("")) {
                mPopularMoviesTextView.setText(s);
            }
        }
    }
}
