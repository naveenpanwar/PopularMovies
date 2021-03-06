package com.example.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.popularmovies.model.Trailer;

import java.util.List;

@Dao
public interface TrailerDao {

    @Query("SELECT * FROM trailer WHERE movie_id = :movieId ")
    LiveData<List<Trailer>> loadTrailers(int movieId);

    @Query("SELECT * FROM trailer WHERE id = :trailerID")
    Trailer getTrailerByID(String trailerID);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTrailer(Trailer trailer);

    @Insert
    void insertTrailer(Trailer trailer);

    @Delete
    void deleteTrailer(Trailer trailer);
}
