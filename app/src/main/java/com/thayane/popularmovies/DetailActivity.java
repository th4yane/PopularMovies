package com.thayane.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thayane.popularmovies.utilities.NetworkUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements SingleMovieTaskInterface {

    @BindView(R.id.tv_title)
    TextView titleTextView;
    @BindView(R.id.tv_release_date)
    TextView releaseDateTextView;
    @BindView(R.id.tv_vote_average)
    TextView voteAverageTextView;
    @BindView(R.id.tv_original_title)
    TextView originalTitleTextView;
    @BindView(R.id.tv_overview)
    TextView overviewTextView;
    @BindView(R.id.tv_error)
    TextView errorMessageTextView;

    @BindView(R.id.iv_poster)
    ImageView posterImageView;

    @BindView(R.id.ll_overview)
    LinearLayout mLinearLayoutOverview;
    @BindView(R.id.ll_basic_info)
    LinearLayout mLinearLayoutInfo;

    private Context context;
    private String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        context = this;
        API_KEY = context.getString(R.string.MOVIEDB_API_KEY);

        Intent it = getIntent();
        if (it.hasExtra(Intent.EXTRA_TEXT)) {
            String id = it.getStringExtra(Intent.EXTRA_TEXT);

            String url = buildUrl(id);

            //Checking if there is internet connection
            if (!isOnline()) {
                errorMessageTextView.setText(R.string.error_connection);
                errorMessageTextView.setVisibility(View.VISIBLE);
            } else {
                errorMessageTextView.setVisibility(View.INVISIBLE);
                URL movieUrl = NetworkUtils.buildMovieUrl(url);
                new SingleMovieTask(this).execute(movieUrl);
            }
        }
    }

    /*Code created with the help of a Stack OverFlow question:
        http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
        */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


    public String buildUrl(String id) {
        String firstPath = "http://api.themoviedb.org/3/movie/";
        String lastPath = "?api_key=" + API_KEY;

        String full = firstPath + id + lastPath;

        return full;
    }

    public String buildImageUrl(String imagePath) {
        String imageUrl = "http://image.tmdb.org/t/p/";
        imageUrl += "w500" + imagePath;
        return imageUrl;
    }

    @Override
    public void onPreExecuteAsyncTask() {
        mLinearLayoutOverview.setVisibility(View.INVISIBLE);
        mLinearLayoutInfo.setVisibility(View.INVISIBLE);

        errorMessageTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPostExecuteAsyncTask(MovieData movieResults) {

        if (movieResults != null) {

            mLinearLayoutOverview.setVisibility(View.VISIBLE);
            mLinearLayoutInfo.setVisibility(View.VISIBLE);
            titleTextView.setText(movieResults.title);
            releaseDateTextView.setText(movieResults.releaseDate);
            voteAverageTextView.setText(movieResults.voteAverage);
            originalTitleTextView.setText(movieResults.originalTitle);
            overviewTextView.setText(movieResults.overview);

            String imageUrl = buildImageUrl(movieResults.imagePath);
            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.image)
                    .error(R.drawable.image_error)
                    .into(posterImageView);
        } else {
            errorMessageTextView.setText(R.string.error_message);
            errorMessageTextView.setVisibility(View.VISIBLE);
        }
    }
}
