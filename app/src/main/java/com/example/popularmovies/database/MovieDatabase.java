package com.example.popularmovies.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.database.DateConverter;

@Database(entities = {Movie.class, Review.class, Trailer.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class MovieDatabase extends RoomDatabase {
    private static final String LOG_TAG = MovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "moviesdatabase";
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "creating new database instance");
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        MovieDatabase.class,
                        MovieDatabase.DATABASE_NAME
                ).build();
            }
        }
        Log.d(LOG_TAG, "getting the database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();
    public abstract ReviewDao reviewDao();
    public abstract TrailerDao trailerDao();
}
