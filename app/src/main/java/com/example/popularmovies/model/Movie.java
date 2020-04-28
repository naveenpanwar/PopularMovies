package com.example.popularmovies.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "movie")
public class Movie {

    @PrimaryKey
    @NonNull
    private int id;

    @ColumnInfo(name = "original_title")
    private String originalTitle;

    private String poster;
    private String plot;
    private double rating;
    private double popularity;

    @ColumnInfo(name = "release_date")
    private Date releaseDate;

    public Movie(int id,String originalTitle, String poster, String plot, double rating, double popularity, Date releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.poster = poster;
        this.plot = plot;
        this.rating = rating;
        this.popularity = popularity;
        this.releaseDate = releaseDate;
    }

    public String toString() {
        return this.originalTitle+" - "+this.rating+" - "+this.releaseDate;
    }

    public String getPoster() {
        return this.poster;
    }

    public int getId() {
        return this.id;
    }

    public String getOriginalTitle() {
        return this.originalTitle;
    }

    public String getPlot() {
        return this.plot;
    }

    public double getRating() {
        return this.rating;
    }

    public double getPopularity() {
        return this.popularity;
    }

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
