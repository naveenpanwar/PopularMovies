package com.example.popularmovies.model;

import java.util.Date;

public class Movie {
    int id;
    String originalTitle;
    String poster;
    String plot;
    double rating;
    String releaseDate;

    public Movie(int iD,String oT, String po, String pl, double rt, String rD) {
        id = iD;
        originalTitle = oT;
        poster = po;
        plot = pl;
        rating = rt;
        releaseDate = rD;
    }

    public String toString() {
        return originalTitle+" - "+rating+" - "+releaseDate;
    }

    public String getImage() {
        return poster;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return originalTitle;
    }

    public String getPlot() {
        return plot;
    }

    public double getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
