package com.thayane.popularmovies.utilities;

import com.thayane.popularmovies.MovieData;
import com.thayane.popularmovies.R;
import com.thayane.popularmovies.ReviewData;

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
        MovieData[] parsedMovieData;

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
    public static MovieData getSingleMovieDataFromJson(String moviesJsonStr, String movieTrailerJsonStr
            , String movieReviewsJsonStr)
            throws JSONException {

        final String MDB_TITLE = "title";
        final String MDB_POSTER = "poster_path";
        final String MDB_ID = "id";
        final String MDB_OVERVIEW = "overview";
        final String MDB_VOTE_AVERAGE = "vote_average";
        final String MDB_RELEASE_DATE = "release_date";
        final String MDB_VIDEO_KEY= "key";
        final String MDB_RUNTIME= "runtime";
        final String MDB_REVIEW_AUTHOR= "author";
        final String MDB_REVIEW= "content";
        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        String id, overview, releaseDate, imagePath, title, voteAverage, runtime;
        String[] video;
        ReviewData[] reviews;

        id = moviesJson.getString(MDB_ID);
        title = moviesJson.getString(MDB_TITLE);
        imagePath = moviesJson.getString(MDB_POSTER);
        overview = moviesJson.getString(MDB_OVERVIEW);
        releaseDate = moviesJson.getString(MDB_RELEASE_DATE);
        voteAverage = moviesJson.getString(MDB_VOTE_AVERAGE);
        runtime = moviesJson.getString(MDB_RUNTIME);

        JSONObject movieTrailerJson = new JSONObject(movieTrailerJsonStr);
        JSONArray result_array = movieTrailerJson.getJSONArray("results");
        int sizeArray = result_array.length();
        video = new String[sizeArray];
        for(int i=0;i < sizeArray;i++){
            JSONObject trailer = result_array.getJSONObject(i);
            video[i] = trailer.getString(MDB_VIDEO_KEY);
        }

        JSONObject movieReviewsJson = new JSONObject(movieReviewsJsonStr);
        JSONArray reviews_array = movieReviewsJson.getJSONArray("results");
        int sizeReviews = reviews_array.length();
        reviews = new ReviewData[sizeReviews];
        for(int i=0;i < sizeReviews;i++){
            JSONObject review = reviews_array.getJSONObject(i);
            reviews[i] = new ReviewData(review.getString(MDB_REVIEW_AUTHOR), review.getString(MDB_REVIEW));
        }

        MovieData data = new MovieData(id, overview, releaseDate, imagePath, title, voteAverage, video, runtime, reviews);
        return data;
    }
}
