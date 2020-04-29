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

import com.example.popularmovies.database.MovieDatabase;
import com.example.popularmovies.model.Movie;
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
        intent.putExtra("poster", movie.getPoster());
        intent.putExtra("title", movie.getOriginalTitle());
        intent.putExtra("plot", movie.getPlot());
        intent.putExtra("rating", String.valueOf(movie.getRating()));
        intent.putExtra("release_date", getStringFromDate(movie.getReleaseDate()));
        intent.putExtra("favorite", String.valueOf(movie.getFavorite()));

        startActivity(intent);
    }


    class PopularMoviesAsyncTask extends AsyncTask<URL, Void, List<Movie>> {
        final private Context mContext;

        PopularMoviesAsyncTask(Context context) {
            mContext = context;
        }

        List<Movie> movies;

        @Override
        protected List<Movie> doInBackground(URL... urls) {
            URL url = urls[0];
            String popularMoviesResults = null;
            try {
                popularMoviesResults = NetworkUtils.fetchMovieDataFromHttp(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                List<Movie> movies = JSONUtils.getMovieListFromJSON(popularMoviesResults);
                for ( int i=0; i < movies.size(); i++) {
                    Movie networkMovie = movies.get(i);
                    Movie dbMovie = mMovieDatabase.movieDao().getMovieById(networkMovie.getId());
                    if(  dbMovie != null) {
                        networkMovie.setFavorite(dbMovie.getFavorite());
                        mMovieDatabase.movieDao().updateMovie(networkMovie);
                    } else {
                        mMovieDatabase.movieDao().insertMovie(networkMovie);
                    }
                }
                return movies;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                mPopularMovies = movies;
                mMoviesAdapter.setMovieList(movies);
                mMoviesAdapter.notifyDataSetChanged();
                Log.d("UPDATED LIST", "" + mPopularMovies.size());
            }
        }
    }
}
