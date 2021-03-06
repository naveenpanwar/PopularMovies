package com.example.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    final private static String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    final private static String PARAM_API_KEY = "api_key";

    final private static String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";

    final private static String YT_IMAGE_BASE_URL = "https://img.youtube.com/vi/";

    final private static String YT_VIDEO_BASE_URL = "https://www.youtube.com/watch?v=";

    public static Uri getImageURL(String image) {
        return Uri.parse(TMDB_IMAGE_BASE_URL+image);
    }

    public static Uri getYouTubeImageURL(String key) {
        return Uri.parse(YT_IMAGE_BASE_URL+key+"/0.jpg");
    }


    private static URL getURL(Uri.Builder builder) {
        Uri uri =  builder.appendQueryParameter(PARAM_API_KEY, SecretCodes.getApiKey()).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /* Please replace SecretCodes.getApiKey() with appropriate key */
    public static URL buildPopularMoviesUrl() {
        Uri.Builder uriBuilder = Uri.parse(TMDB_BASE_URL+"popular").buildUpon();
        return getURL(uriBuilder);
    }

    /* Please replace SecretCodes.getApiKey() with appropriate key */
    public static URL buildReviewsUrl(int id) {
        Uri.Builder uriBuilder = Uri.parse(TMDB_BASE_URL+id+"/reviews").buildUpon();
        return getURL(uriBuilder);
    }

    /* Please replace SecretCodes.getApiKey() with appropriate key */
    public static URL buildTrailersUrl(int id) {
        Uri.Builder uriBuilder = Uri.parse(TMDB_BASE_URL+id+"/videos").buildUpon();
        return getURL(uriBuilder);
    }

    public static URL buildTopRatedMoviesUrl() {
        Uri.Builder uriBuilder = Uri.parse(TMDB_BASE_URL+"top_rated").buildUpon();
        return getURL(uriBuilder);
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

    public static String getYouTubeURL(String key) {
        String YtUrl = YT_VIDEO_BASE_URL+key;
        return YtUrl;
    }
}


