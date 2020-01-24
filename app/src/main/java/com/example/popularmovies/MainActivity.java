package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

    private List<Movie> mPopularMovies;

    private RecyclerView mPopularMoviesRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPopularMoviesRecyclerView = findViewById(R.id.rv_popular_movies);

        getPopularMovies();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        mPopularMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        mPopularMoviesRecyclerView.setHasFixedSize(true);

        if( mPopularMovies != null ) {
            Log.d("INITIAL ADAPTER LIST", "" + mPopularMovies.size()+"YES");
        }
        else {
            Log.d("INITIAL ADAPTER LIST", "" + 0+"NO");
        }
        mMoviesAdapter =  new MoviesAdapter();
        mPopularMoviesRecyclerView.setAdapter(mMoviesAdapter);
    }

    private void getPopularMovies() {
        URL url = NetworkUtils.buildUrl();
        new PopularMoviesAsyncTask(this).execute(url);
    }

    public class PopularMoviesAsyncTask extends AsyncTask<URL,Void, String> {
        private Context mContext;

        public PopularMoviesAsyncTask(Context context) {
            mContext = context;
        }

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
                    mPopularMovies = JSONUtils.getMovieListFromJSON(s);
                    mMoviesAdapter.setMovieList(mPopularMovies);
                    mMoviesAdapter.notifyDataSetChanged();
                    Log.d("UPDATED LIST",""+mPopularMovies.size());
                } catch (JSONException e) {
                    mPopularMovies = null;
                    e.printStackTrace();
                }
            }
        }
    }
}
