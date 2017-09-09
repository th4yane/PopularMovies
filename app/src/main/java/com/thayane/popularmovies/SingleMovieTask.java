package com.thayane.popularmovies;

import android.os.AsyncTask;

import com.thayane.popularmovies.utilities.MovieDbJsonUtils;
import com.thayane.popularmovies.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by Thayane on 17/02/2017.
 */

public class SingleMovieTask extends AsyncTask<URL, Void, MovieData>{
    private SingleMovieTaskInterface listener;

    public SingleMovieTask(SingleMovieTaskInterface listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPreExecuteAsyncTask();
    }

    @Override
    protected MovieData doInBackground(URL... params) {

        if (params.length == 0) {
            return null;
        }

        URL movieUrl = params[0];
        URL movieTrailerUrl = params[1];
        URL movieReviewsUrl = params[2];

        try {
            String jsonMovieResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieUrl);

            String jsonMovieTrailerResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieTrailerUrl);

            String jsonMovieReviewsResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieReviewsUrl);

            MovieData simpleJsonMovieData = MovieDbJsonUtils
                    .getSingleMovieDataFromJson(jsonMovieResponse, jsonMovieTrailerResponse,
                            jsonMovieReviewsResponse);

            return simpleJsonMovieData;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(MovieData movieResults) {
        listener.onPostExecuteAsyncTask(movieResults);
    }
}