package com.thayane.popularmovies;

/**
 * Created by Thayane on 17/02/2017.
 */

public interface SingleMovieTaskInterface {
    void onPreExecuteAsyncTask();
    void onPostExecuteAsyncTask(MovieData movieResults);
}
