package com.example.popularmovies.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.popularmovies.model.Review;

import java.util.List;

@Dao
public interface ReviewDao {

    @Query("SELECT * FROM review WHERE movie_id = :movieId ")
    List<Review> loadReviews(int movieId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateReview(Review review);

    @Insert
    void insertReview(Review review);

    @Delete
    void deleteReview(Review review);
}
