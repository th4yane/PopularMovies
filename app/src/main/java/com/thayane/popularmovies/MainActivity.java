package com.thayane.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thayane.popularmovies.utilities.MovieDbJsonUtils;
import com.thayane.popularmovies.utilities.NetworkUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler,MovieTaskInterface{

    @BindView(R.id.recyclerview_movies) RecyclerView moviesRecyclerView;
    @BindView(R.id.pb_loading_movies) ProgressBar mProgressBar;
    @BindView(R.id.tv_error_message) TextView errorMessageTextView;

    private static MoviesAdapter mMovieAdapter;
    private GridLayoutManager layoutManager;
    private String API_KEY;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        context = this;
        API_KEY = context.getString(R.string.MOVIEDB_API_KEY);

        layoutManager = new GridLayoutManager(this, calculateNoOfColumns(this), GridLayoutManager.VERTICAL,false);

        if(mMovieAdapter == null){
            mMovieAdapter = new MoviesAdapter(this, this);
            popularMovies(); //default sort by popular movies
        }
        moviesRecyclerView.setAdapter(mMovieAdapter);
        moviesRecyclerView.setLayoutManager(layoutManager);

    }

    /*Code created with help of a Stack Overflow question:
    http://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 120);
        return noOfColumns;
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
        //Checking if there is internet connection
        if(!isOnline()){
            errorMessageTextView.setText(context.getString(R.string.error_connection));
            errorMessageTextView.setVisibility(View.VISIBLE);
        } else {
            errorMessageTextView.setVisibility(View.INVISIBLE);
            URL movieUrl = NetworkUtils.buildTopRatedUrl(API_KEY);
            new MovieTask(this).execute(movieUrl);
        }
    }

    public void popularMovies(){
        //Checking if there is internet connection
        if(!isOnline()){
            errorMessageTextView.setText(context.getString(R.string.error_connection));
            errorMessageTextView.setVisibility(View.VISIBLE);
        } else{
            errorMessageTextView.setVisibility(View.INVISIBLE);
            URL movieUrl = NetworkUtils.buildPopularUrl(API_KEY);
            new MovieTask(this).execute(movieUrl);
        }
    }

    /*Code created with the help of a Stack OverFlow question:
        http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
        */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public void onPreExecuteAsyncTask() {
        mProgressBar.setVisibility(View.VISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPostExecuteAsyncTask(MovieData[] moviesResults) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (moviesResults != null) {
            mMovieAdapter.setMoviesData(moviesResults);
        } else{
            errorMessageTextView.setText(context.getString(R.string.error_message));
            errorMessageTextView.setVisibility(View.VISIBLE);
        }
    }
}
