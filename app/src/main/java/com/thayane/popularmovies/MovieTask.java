package com.thayane.popularmovies;

import android.os.AsyncTask;

import com.thayane.popularmovies.utilities.MovieDbJsonUtils;
import com.thayane.popularmovies.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by Thayane on 17/02/2017.
 */

public class MovieTask extends AsyncTask<URL, Void, MovieData[]> {

    private MovieTaskInterface listener;

    public MovieTask(MovieTaskInterface listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPreExecuteAsyncTask();
    }

    @Override
    protected MovieData[] doInBackground(URL... params) {

        if (params.length == 0) {
            return null;
        }

        URL movieUrl = params[0];

        try {
            String jsonMovieResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieUrl);

            MovieData[] simpleJsonMovieData = MovieDbJsonUtils
                    .getMoviesDataFromJson(jsonMovieResponse);

            return simpleJsonMovieData;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(MovieData[] moviesResults) {
        listener.onPostExecuteAsyncTask(moviesResults);
    }

}
