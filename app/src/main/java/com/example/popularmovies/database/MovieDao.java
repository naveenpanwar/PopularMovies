package com.example.popularmovies.database;

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

    @Query("SELECT * FROM movie ORDER BY popularity")
    List<Movie> loadMoviesByPopularity();

    @Query("SELECT * FROM movie ORDER BY rating")
    List<Movie> loadMoviesByTopRating();

    @Query("SELECT * FROM movie WHERE id = :movieID")
    Movie getMovieById(int movieID);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}
