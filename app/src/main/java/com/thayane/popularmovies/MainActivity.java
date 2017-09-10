package com.thayane.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thayane.popularmovies.data.MoviesContract;
import com.thayane.popularmovies.utilities.NetworkUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        MoviesAdapter.MoviesAdapterOnClickHandler, MovieTaskInterface {

    @BindView(R.id.recyclerview_movies)
    RecyclerView moviesRecyclerView;
    @BindView(R.id.pb_loading_movies)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_error_message)
    TextView errorMessageTextView;

    private MoviesAdapter mMovieAdapter;
    private MovieCursorAdapter mMovieDataAdapter;
    private GridLayoutManager layoutManager;
    private String API_KEY;
    private Context context;
    private boolean mTwoPane;
    private static final String DETAILACTIVITY_TAG = "DATAG";
    private static Bundle bd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
        ButterKnife.bind(this);

        context = this;
        API_KEY = context.getString(R.string.MOVIEDB_API_KEY);

        layoutManager = new GridLayoutManager(this, calculateNoOfColumns(this), GridLayoutManager.VERTICAL, false);

        if (mMovieAdapter == null) {
            if (mMovieDataAdapter != null) {
                moviesRecyclerView.setAdapter(mMovieDataAdapter);
            } else {
                popularMovies(); //default sort by popular movies
            }
        } else {
            moviesRecyclerView.setAdapter(mMovieAdapter);
        }

        moviesRecyclerView.setLayoutManager(layoutManager);
        bd = savedInstanceState;

    }

    @Override
    public void clickHandler(String id) {

        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putString(DetailFragment.DETAIL_ID, id);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILACTIVITY_TAG)
                    .commit();

        } else {
            Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
            intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, id);
            startActivity(intentToStartDetailActivity);
        }
    }


    /*Code created with help of a Stack Overflow question:
    http://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns */
    public int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        if (mTwoPane) {
            dpWidth = dpWidth - ((dpWidth + 110) / 2);
        }
        int noOfColumns = (int) (dpWidth / 130);
        return noOfColumns;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_most_popular:
                mMovieAdapter = null;
                mMovieDataAdapter = null;
                popularMovies();
                return true;
            case R.id.action_top_rated:
                mMovieAdapter = null;
                mMovieDataAdapter = null;
                topRatedMovies();
                return true;
            case R.id.action_favorites:
                mMovieAdapter = null;
                favoriteMovies();
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

    public void topRatedMovies() {
        //Checking if there is internet connection
        if (!isOnline()) {
            errorMessageTextView.setText(context.getString(R.string.error_connection));
            errorMessageTextView.setVisibility(View.VISIBLE);
            showSnackBar();
        } else {
            errorMessageTextView.setVisibility(View.INVISIBLE);
            URL movieUrl = NetworkUtils.buildTopRatedUrl(API_KEY);
            new MovieTask(this).execute(movieUrl);
        }
    }

    public void popularMovies() {
        //Checking if there is internet connection
        if (!isOnline()) {
            errorMessageTextView.setText(context.getString(R.string.error_connection));
            errorMessageTextView.setVisibility(View.VISIBLE);
            showSnackBar();
        } else {
            errorMessageTextView.setVisibility(View.INVISIBLE);
            URL movieUrl = NetworkUtils.buildPopularUrl(API_KEY);
            new MovieTask(this).execute(movieUrl);
        }
    }

    public void favoriteMovies() {
        errorMessageTextView.setVisibility(View.INVISIBLE);
        Cursor c = this.getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        mMovieDataAdapter = new MovieCursorAdapter(this, c);
        moviesRecyclerView.setAdapter(mMovieDataAdapter);
    }

    public void showSnackBar(){
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout_main);
        Snackbar snackbar = Snackbar.make(frameLayout, getString(R.string.error_connection), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.retry_connection), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popularMovies();
            }
        });
        snackbar.show();
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
            mMovieAdapter = new MoviesAdapter(this, this);
            mMovieAdapter.setMoviesData(moviesResults);
            moviesRecyclerView.setAdapter(mMovieAdapter);
        } else {
            errorMessageTextView.setText(context.getString(R.string.error_message));
            errorMessageTextView.setVisibility(View.VISIBLE);
        }
    }
}
