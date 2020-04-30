package com.example.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.popularmovies.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY popularity DESC")
    LiveData<List<Movie>> loadMoviesByPopularity();

    @Query("SELECT * FROM movie ORDER BY rating DESC")
    LiveData<List<Movie>> loadMoviesByTopRating();

    @Query("SELECT * FROM movie WHERE id = :movieID")
    LiveData<Movie> getMovieById(int movieID);

    @Query("SELECT * FROM movie WHERE id = :movieID")
    Movie getMovieByIdSimple(int movieID);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}
