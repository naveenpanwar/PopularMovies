package com.example.popularmovies.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trailer")
public class Trailer {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "movie_id")
    private int movieId;

    private String key;
    private String name;
    private String site;

    public Trailer(String id, int movieId, String key, String name, String site) {
        this.id = id;
        this.movieId = movieId;
        this.key = key;
        this.name = name;
        this.site = site;
    }

    public String getId() {
        return this.id;
    }

    public int getMovieId() {
        return this.movieId;
    }

    public String getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

    public String getSite() {
        return this.site;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
