package com.thayane.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Thayane on 10/02/2017.
 */

public class NetworkUtils  {

    private static final String POPULAR_MOVIES_URL =
            "http://api.themoviedb.org/3/movie/popular?api_key=";

    private static final String TOP_RATED_MOVIES_URL =
            "http://api.themoviedb.org/3/movie/top_rated?api_key=";

    public static URL buildPopularUrl(String key) {
        Uri builtUri = Uri.parse(POPULAR_MOVIES_URL + key).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildTopRatedUrl(String key) {
        Uri builtUri = Uri.parse(TOP_RATED_MOVIES_URL + key).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /* Method that gets a url of a single movie,
                        based on his ID
      */
    public static URL buildMovieUrl(String movieUrl) {
        Uri builtUri = Uri.parse(movieUrl).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
