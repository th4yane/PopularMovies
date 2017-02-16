package com.thayane.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thayane.popularmovies.utilities.MovieDbJsonUtils;
import com.thayane.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler{

    private RecyclerView moviesRecyclerView;
    private static MoviesAdapter mMovieAdapter;
    private ProgressBar mProgressBar;
    private TextView errorMessageTextView;
    private GridLayoutManager layoutManager;
    private String API_KEY;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        API_KEY = context.getString(R.string.MOVIEDB_API_KEY);

        moviesRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_movies);

        errorMessageTextView = (TextView) findViewById(R.id.tv_error_message);

        layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL,false);


        //check screen orientation
        if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE){
            layoutManager.setSpanCount(5);
        }

        if(mMovieAdapter == null){
            mMovieAdapter = new MoviesAdapter(this, this);
            popularMovies(); //default sort by popular movies
        }
        moviesRecyclerView.setAdapter(mMovieAdapter);
        moviesRecyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void clickHandler(String id) {
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, id);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_most_popular:
                mMovieAdapter.setMoviesData(null);
                popularMovies();
                return true;
            case R.id.action_top_rated:
                mMovieAdapter.setMoviesData(null);
                topRatedMovies();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void topRatedMovies(){
        URL movieUrl = NetworkUtils.buildTopRatedUrl(API_KEY);
        new MovieTask().execute(movieUrl);
    }

    public void popularMovies(){
        URL movieUrl = NetworkUtils.buildPopularUrl(API_KEY);
        new MovieTask().execute(movieUrl);
    }


    public class MovieTask extends AsyncTask<URL, Void, MovieData[]> {

        String error;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            error = context.getString(R.string.error_message);
            mProgressBar.setVisibility(View.VISIBLE);
            errorMessageTextView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected MovieData[] doInBackground(URL... params) {

            if (params.length == 0) {
                return null;
            }
            if(!isOnline()){
                error = context.getString(R.string.error_connection);
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
            mProgressBar.setVisibility(View.INVISIBLE);
            if (moviesResults != null) {
                mMovieAdapter.setMoviesData(moviesResults);
            } else{
                errorMessageTextView.setText(error);
                errorMessageTextView.setVisibility(View.VISIBLE);
            }

        }

        /*Code created with the help of a Stack OverFlow question:
        http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
        */
        public boolean isOnline() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
    }
}
