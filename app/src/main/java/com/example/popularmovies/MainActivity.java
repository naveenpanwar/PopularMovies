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

        mMoviesAdapter = new MoviesAdapter(this);
        mPopularMoviesRecyclerView.setAdapter(mMoviesAdapter);

        getPopularMoviesFromNetwork();
        getTopRatedMoviesFromNetwork();

        getTopRatedMovies();
    }

    private void getPopularMoviesFromNetwork() {
        // populate popular movies from network to db
        FetchMoviesFromNetwork.getPopularMovies(getApplicationContext());
    }

    private void getTopRatedMoviesFromNetwork() {
        // populate top rated movies from network to db
        FetchMoviesFromNetwork.getTopRatedMovies(getApplicationContext());
    }

    private void getPopularMovies() {
        LiveData<List<Movie>> movies = mMovieDatabase.movieDao().loadMoviesByPopularity();
        movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                Log.d("MAIN OBSERVER", "DATASET CHANGED");
                mMoviesAdapter.setMovieList(movies);
                mMoviesAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getTopRatedMovies() {
        LiveData<List<Movie>> movies = mMovieDatabase.movieDao().loadMoviesByTopRating();
        movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                mMoviesAdapter.setMovieList(movies);
                mMoviesAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getFavoriteMovies() {
        LiveData<List<Movie>> movies = mMovieDatabase.movieDao().loadMoviesByFavorite();
        movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                mMoviesAdapter.setMovieList(movies);
                mMoviesAdapter.notifyDataSetChanged();
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
        } else if (id == R.id.action_sort_by_favorite) {
            getFavoriteMovies();
        } else {
            getTopRatedMovies();
        }
        return true;
    }

    @Override
    public void onMovieItemClick(int movieId) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("id", String.valueOf(movieId));
        Log.d("MOVIE MAIN ACT", String.valueOf(movieId));
        startActivity(intent);
    }
}
