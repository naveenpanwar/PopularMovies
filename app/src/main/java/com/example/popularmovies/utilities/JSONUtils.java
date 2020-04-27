package com.example.popularmovies.utilities;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static List<Movie> getMovieListFromJSON(String json) throws JSONException {

        List<Movie> movieList = new ArrayList<>();

        JSONObject responseJSON = new JSONObject(json);

        if ( responseJSON.has("results") ) {
            JSONArray results = responseJSON.getJSONArray("results");
            for( int i=0; i<results.length(); i++) {

                JSONObject movie = results.getJSONObject(i);
                int id = movie.getInt("id");
                String oT = movie.getString("original_title");
                String po = movie.getString("poster_path");
                String pl = movie.getString("overview");
                double rt = movie.getDouble("vote_average");
                String rD = movie.getString("release_date");

                Movie m = new Movie(id, oT,po,pl,rt,rD);

                movieList.add(m);
            }
        }

        return movieList;
    }

    public static List<Review> getReviewListFromJSON(String json) throws JSONException {

        List<Review> reviewList = new ArrayList<>();

        JSONObject responseJSON = new JSONObject(json);

        if ( responseJSON.has("results") ) {
            JSONArray results = responseJSON.getJSONArray("results");
            for( int i=0; i<results.length(); i++) {

                JSONObject review = results.getJSONObject(i);
                String id = review.getString("id");
                String aU = review.getString("author");
                String cO = review.getString("content");
                String uRL = review.getString("url");

                Review r = new Review(id, aU, cO, uRL);

                reviewList.add(r);
            }
        }

        return reviewList;
    }

    public static List<Trailer> getTrailersListFromJSON(String json) throws JSONException {

        List<Trailer> trailersList = new ArrayList<>();

        JSONObject responseJSON = new JSONObject(json);

        if ( responseJSON.has("results") ) {
            JSONArray results = responseJSON.getJSONArray("results");
            for( int i=0; i<results.length(); i++) {

                JSONObject trailer = results.getJSONObject(i);
                String id = trailer.getString("id");
                String kY = trailer.getString("key");
                String nM = trailer.getString("name");
                String sT = trailer.getString("site");

                if ( sT.toLowerCase().equals("youtube") ) {
                    Trailer t = new Trailer(id, kY, nM, sT);
                    trailersList.add(t);
                }
            }
        }

        return trailersList;
    }
}
