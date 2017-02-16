package com.thayane.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thayane.popularmovies.utilities.MovieDbJsonUtils;
import com.thayane.popularmovies.utilities.NetworkUtils;

import org.w3c.dom.Text;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private TextView titleTextView, releaseDateTextView, voteAverageTextView, originalTitleTextView, overviewTextView, errorMessageTextView;
    private ImageView posterImageView;
    private Context context;
    private LinearLayout mLinearLayoutOverview, mLinearLayoutInfo;
    private String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        context = this;
        API_KEY = context.getString(R.string.MOVIEDB_API_KEY);

        titleTextView = (TextView) findViewById(R.id.tv_title);
        releaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        voteAverageTextView = (TextView) findViewById(R.id.tv_vote_average);
        originalTitleTextView = (TextView) findViewById(R.id.tv_original_title);
        overviewTextView = (TextView) findViewById(R.id.tv_overview);
        errorMessageTextView = (TextView) findViewById(R.id.tv_error);

        posterImageView = (ImageView) findViewById(R.id.iv_poster);

        mLinearLayoutOverview = (LinearLayout) findViewById(R.id.ll_overview);
        mLinearLayoutInfo = (LinearLayout) findViewById(R.id.ll_basic_info);


        Intent it = getIntent();
        if (it.hasExtra(Intent.EXTRA_TEXT)) {
            String id = it.getStringExtra(Intent.EXTRA_TEXT);

            String url = buildUrl(id);

            URL movieUrl = NetworkUtils.buildMovieUrl(url);
            new SingleMovieTask().execute(movieUrl);
        }
    }

    public String buildUrl(String id) {
        String firstPath = "http://api.themoviedb.org/3/movie/";
        String lastPath = "?api_key=" + API_KEY;

        String full = firstPath + id + lastPath;

        return full;
    }

    public String buildImageUrl(String imagePath, String size){
        String imageUrl = "http://image.tmdb.org/t/p/";
        imageUrl += size + imagePath;
        return imageUrl;
    }

    public class SingleMovieTask extends AsyncTask<URL, Void, MovieData> {

        String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            error = context.getString(R.string.error_message);
            mLinearLayoutOverview.setVisibility(View.INVISIBLE);
            mLinearLayoutInfo.setVisibility(View.INVISIBLE);
            errorMessageTextView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected MovieData doInBackground(URL... params) {

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

                MovieData simpleJsonMovieData = MovieDbJsonUtils
                        .getSingleMovieDataFromJson(jsonMovieResponse);

                return simpleJsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieData movieResults) {

            if (movieResults != null) {
                mLinearLayoutOverview.setVisibility(View.VISIBLE);
                mLinearLayoutInfo.setVisibility(View.VISIBLE);

                titleTextView.setText(movieResults.title);
                releaseDateTextView.setText(movieResults.releaseDate);
                voteAverageTextView.setText(movieResults.voteAverage);
                originalTitleTextView.setText(movieResults.originalTitle);
                overviewTextView.setText(movieResults.overview);

                String imageUrl = buildImageUrl(movieResults.imagePath, "w500");
                Picasso.with(context).load(imageUrl).into(posterImageView);
            } else {
                errorMessageTextView.setText(error);
                errorMessageTextView.setVisibility(View.VISIBLE);
            }
        }

        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }

    }
}
