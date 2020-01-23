package com.example.popularmovies.utilities;

import com.example.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JSONUtils {

    public static List<Movie> getMovieListFromJSON(String json) throws JSONException {

        List<Movie> movieList = new ArrayList<>();

        JSONObject responseJSON = new JSONObject(json);

        if ( responseJSON.has("results") ) {
            JSONArray results = responseJSON.getJSONArray("results");
            for( int i=0; i<results.length(); i++) {

                JSONObject movie = results.getJSONObject(i);
                Movie m = null;

                String oT = movie.getString("original_title");
                String po = movie.getString("poster_path");
                String pl = movie.getString("overview");
                int rt = movie.getInt("vote_average");
                String rD = movie.getString("release_date");

                m = new Movie(oT,po,pl,rt,rD);

                movieList.add(m);
            }
        }

        return movieList;
    }
}
