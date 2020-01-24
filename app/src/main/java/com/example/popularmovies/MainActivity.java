package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utilities.JSONUtils;
import com.example.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClickListener {

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
        mMoviesAdapter =  new MoviesAdapter(this);
        mPopularMoviesRecyclerView.setAdapter(mMoviesAdapter);
    }

    private void getPopularMovies() {
        URL url = NetworkUtils.buildPopularMoviesUrl();
        new PopularMoviesAsyncTask(this).execute(url);
    }

    private void getTopRatedMovies() {
        URL url = NetworkUtils.buildTopRatedMoviesUrl();
        new PopularMoviesAsyncTask(this).execute(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sorting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.action_sort_by_popularity) {
            getPopularMovies();
        }
        else {
            getTopRatedMovies();
        }

        return true;
    }

    @Override
    public void onMovieItemClick(int clickedMovieIndex) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        Movie movie = mPopularMovies.get(clickedMovieIndex);

        intent.putExtra("id", String.valueOf(movie.getId()));
        intent.putExtra("poster", movie.getImage());
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("plot", movie.getPlot());
        intent.putExtra("rating", String.valueOf(movie.getRating()));
        intent.putExtra("release_date", movie.getReleaseDate());

        startActivity(intent);
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
