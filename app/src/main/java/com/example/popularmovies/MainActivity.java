package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utilities.JSONUtils;
import com.example.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

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
                try {
                    List<Movie> movies = JSONUtils.getMovieListFromJSON(s);
                    for (int i=0;i<movies.size(); i++) {
                        Movie movie = movies.get(i);
                        mPopularMoviesTextView.append(movie.toString()+"\n\n\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mPopularMoviesTextView.setText("Oooops! Something went wrong");
                }
            }
        }
    }
}
