package com.example.popularmovies.model;

public class Movie {
    final private int id;
    final private String originalTitle;
    final private String poster;
    final private String plot;
    final private double rating;
    final private String releaseDate;

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
