package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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

import com.example.popularmovies.database.MovieDatabase;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.network.FetchMoviesFromNetwork;
import com.example.popularmovies.utilities.JSONUtils;
import com.example.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import static com.example.popularmovies.database.DateConverter.getStringFromDate;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClickListener {

    private List<Movie> mPopularMovies;

    private RecyclerView mPopularMoviesRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    private MovieDatabase mMovieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPopularMoviesRecyclerView = findViewById(R.id.rv_popular_movies);
        mMovieDatabase = MovieDatabase.getInstance(getApplicationContext());

        getPopularMovies();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mPopularMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        mPopularMoviesRecyclerView.setHasFixedSize(true);

        if (mPopularMovies != null) {
            Log.d("INITIAL ADAPTER LIST", "" + mPopularMovies.size() + "YES");
        } else {
            Log.d("INITIAL ADAPTER LIST", "" + 0 + "NO");
        }
        mMoviesAdapter = new MoviesAdapter(this);
        mPopularMoviesRecyclerView.setAdapter(mMoviesAdapter);

        getPopularMoviesFromNetwork();
        getTopRatedMoviesFromNetwork();

        getPopularMovies();
    }

    private void getPopularMoviesFromNetwork() {
        // populate popular movies from network to db
        MovieExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                FetchMoviesFromNetwork fetchMoviesFromNetwork = new FetchMoviesFromNetwork(getApplicationContext());
                fetchMoviesFromNetwork.getPopularMovies();
            }
        });
    }

    private void getTopRatedMoviesFromNetwork() {
        // populate top rated movies from network to db
        MovieExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                FetchMoviesFromNetwork fetchMoviesFromNetwork = new FetchMoviesFromNetwork(getApplicationContext());
                fetchMoviesFromNetwork.getTopRatedMovies();
            }
        });
    }

    private void getPopularMovies() {
        LiveData<List<Movie>> movies = mMovieDatabase.movieDao().loadMoviesByTopRating();
        movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                mMoviesAdapter.setMovieList(movies);
            }
        });
    }

    private void getTopRatedMovies() {
        LiveData<List<Movie>> movies = mMovieDatabase.movieDao().loadMoviesByPopularity();
        movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                mMoviesAdapter.setMovieList(movies);
            }
        });
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
        if (id == R.id.action_sort_by_popularity) {
            getPopularMovies();
        } else {
            getTopRatedMovies();
        }

        return true;
    }

    @Override
    public void onMovieItemClick(int clickedMovieIndex) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        Movie movie = mPopularMovies.get(clickedMovieIndex);

        intent.putExtra("id", String.valueOf(movie.getId()));

        startActivity(intent);
    }
}
