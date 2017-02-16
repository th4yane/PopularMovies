package com.thayane.popularmovies.utilities;

import com.thayane.popularmovies.MovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Thayane on 10/02/2017.
 */

public class MovieDbJsonUtils {

    /*This method gets all the movies posters path and id*/
    public static MovieData[] getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        final String MDB_RESULTS = "results";

        final String MDB_POSTER = "poster_path";
        final String MDB_ID = "id";

        /* String array to hold each movie's data */
        MovieData[] parsedMovieData = null;

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        JSONArray moviesArray = moviesJson.getJSONArray(MDB_RESULTS);

        parsedMovieData = new MovieData[moviesArray.length()];


        for (int i = 0; i < moviesArray.length(); i++) {

            String id, imagePath;

            /* Get the JSON object representing the movie */
            JSONObject movieInfo = moviesArray.getJSONObject(i);

            imagePath = movieInfo.getString(MDB_POSTER);
            id = movieInfo.getString(MDB_ID);

            /* create a new MovieData object
                    containing only the image path and id
             */
            MovieData movieData = new MovieData(imagePath, id);

            parsedMovieData[i] = movieData;
        }

        return parsedMovieData;
    }

    /* This method gets all information need about a single movie*/
    public static MovieData getSingleMovieDataFromJson(String moviesJsonStr)
            throws JSONException {

        final String MDB_TITLE = "title";
        final String MDB_POSTER = "poster_path";
        final String MDB_ID = "id";
        final String MDB_OVERVIEW = "overview";
        final String MDB_ORIGINAL_TITLE = "original_title";
        final String MDB_VOTE_AVERAGE = "vote_average";
        final String MDB_RELEASE_DATE = "release_date";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);



        String id, overview, releaseDate, imagePath, originalTitle, title, voteAverage;


        id = moviesJson.getString(MDB_ID);
        title = moviesJson.getString(MDB_TITLE);
        imagePath = moviesJson.getString(MDB_POSTER);
        overview = moviesJson.getString(MDB_OVERVIEW);
        releaseDate = moviesJson.getString(MDB_RELEASE_DATE);
        originalTitle = moviesJson.getString(MDB_ORIGINAL_TITLE);
        voteAverage = moviesJson.getString(MDB_VOTE_AVERAGE);

        MovieData data = new MovieData(id, overview, releaseDate, imagePath, originalTitle, title, voteAverage);
        return data;
    }
}
