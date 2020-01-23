package com.example.popularmovies.model;

import java.util.Date;

public class Movie {
    String originalTitle;
    String poster;
    String plot;
    int rating;
    String releaseDate;

    public Movie(String oT, String po, String pl, int rt, String rD) {
        originalTitle = oT;
        poster = po;
        plot = pl;
        rating = rt;
        releaseDate = rD;
    }

    public String toString() {
        return originalTitle+" - "+rating+" - "+releaseDate;
    }
}
