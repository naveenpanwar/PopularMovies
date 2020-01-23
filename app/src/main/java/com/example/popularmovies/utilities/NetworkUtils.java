package com.example.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    final static String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/popular";
    final static String PARAM_API_KEY = "api_key";


    /* Please replace SecretCodes.getApiKey() with appropriate key */
    public static URL buildUrl() {
        Uri uri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, SecretCodes.getApiKey())
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String fetchMovieDataFromHttp(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner sc = new Scanner(in);
            sc.useDelimiter("\\A");

            boolean hasInput = sc.hasNext();
            if ( hasInput ) {
                return sc.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}


