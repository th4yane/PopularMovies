package com.thayane.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thayane.popularmovies.data.MoviesContract;
import com.thayane.popularmovies.utilities.NetworkUtils;
import com.thayane.popularmovies.utilities.Utility;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Thayane on 27/08/2017.
 */

public class DetailFragment extends Fragment implements SingleMovieTaskInterface {
    @BindView(R.id.tv_title)
    TextView titleTextView;
    @BindView(R.id.tv_release_date)
    TextView releaseDateTextView;
    @BindView(R.id.tv_vote_average)
    TextView voteAverageTextView;
    @BindView(R.id.tv_overview)
    TextView overviewTextView;
    @BindView(R.id.tv_error)
    TextView errorMessageTextView;
    @BindView(R.id.tv_runtime)
    TextView runtimeTextView;

    @BindView(R.id.iv_poster)
    ImageView posterImageView;

    @BindView(R.id.ll_overview)
    LinearLayout mLinearLayoutOverview;
    @BindView(R.id.ll_basic_info)
    LinearLayout mLinearLayoutInfo;

    @BindView(R.id.recyclerview_trailers)
    RecyclerView trailersRecyclerView;

    @BindView(R.id.recyclerview_reviews)
    RecyclerView reviewsRecyclerView;

    @BindView(R.id.mark_favorite_btn)
    Button markFavoriteButton;

    private Context context;
    private String API_KEY;
    private static TrailersAdapter mTrailersAdapter;
    private static ReviewsAdapter mReviewsAdapter;
    private LinearLayoutManager layoutManager;
    private String id, imagePath;
    private static boolean favorite;
    static final String DETAIL_ID = "ID";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getString(DetailFragment.DETAIL_ID);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();
        API_KEY = context.getString(R.string.MOVIEDB_API_KEY);
        favorite = true;

        if (mTrailersAdapter == null) {
            mTrailersAdapter = new TrailersAdapter(context);
        }
        if (mReviewsAdapter == null) {
            mReviewsAdapter = new ReviewsAdapter(context);
        }

        String url = buildUrl(id);
        String trailerUrl = buildMovieUrl(id);
        String reviewsUrl = buildMovieReviewsUrl(id);

        //Checking if there is internet connection
        if (!isOnline()) {
            errorMessageTextView.setText(R.string.error_connection);
            errorMessageTextView.setVisibility(View.VISIBLE);
        } else {
            errorMessageTextView.setVisibility(View.INVISIBLE);
            URL[] movieUrl = {NetworkUtils.buildMovieUrl(url), NetworkUtils.buildMovieTrailerUrl(trailerUrl),
                    NetworkUtils.buildMovieUrl(reviewsUrl)};
            new SingleMovieTask(this).execute(movieUrl);
        }

        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
        trailersRecyclerView.setAdapter(mTrailersAdapter);
        trailersRecyclerView.setLayoutManager(layoutManager);

        reviewsRecyclerView.setAdapter(mReviewsAdapter);
        reviewsRecyclerView.setLayoutManager( new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));

        favorite = checkIfFavorite();
        if(favorite){
            markFavoriteButton.setText(context.getString(R.string.button_unfavorite));
        }

        markFavoriteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(favorite){
                    deleteData(id);
                    markFavoriteButton.setText(context.getString(R.string.button_favorite));
                }else{
                    MovieData favorite = new MovieData(id, titleTextView.getText().toString(),imagePath);
                    insertData(favorite);
                    markFavoriteButton.setText(context.getString(R.string.button_unfavorite));
                }
            }
        });

    }

    /*Code created with the help of a Stack OverFlow question:
        http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
        */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }



    public boolean checkIfFavorite(){
        Cursor c = context.getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                null,
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{id},
                null);
        if (c.getCount() != 0){
            return true;
        } else{
            return false;
        }
    }

    public String buildUrl(String id) {
        String firstPath = "http://api.themoviedb.org/3/movie/";
        String lastPath = "?api_key=" + API_KEY;

        String full = firstPath + id + lastPath;

        return full;
    }

    public String buildMovieUrl(String id) {
        String firstPath = "http://api.themoviedb.org/3/movie/";
        String lastPath = "/videos?api_key=" + API_KEY;

        String full = firstPath + id + lastPath;

        return full;
    }

    public String buildMovieReviewsUrl(String id) {
        String firstPath = "http://api.themoviedb.org/3/movie/";
        String lastPath = "/reviews?api_key=" + API_KEY;

        String full = firstPath + id + lastPath;

        return full;
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
            releaseDateTextView.setText(Utility.formatDate(movieResults.releaseDate));
            voteAverageTextView.setText(movieResults.voteAverage + " /10");
            overviewTextView.setText(movieResults.overview);
            runtimeTextView.setText(movieResults.runtime + "min");

            String[] videos = movieResults.video;
            mTrailersAdapter.setTrailersData(videos);

            ReviewData[] reviews = movieResults.reviews;
            mReviewsAdapter.setReviewsData(reviews);

            String imageUrl = Utility.buildImageUrl(movieResults.imagePath);
            this.imagePath = movieResults.imagePath;

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

    public void insertData(MovieData favorite) {
        ContentValues favoriteValue = new ContentValues();

        favoriteValue.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, favorite.id);
        favoriteValue.put(MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE,
                favorite.title);
        favoriteValue.put(MoviesContract.MovieEntry.COLUMN_IMAGE_PATH,
                favorite.imagePath);

        context.getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI,
                favoriteValue);
    }

    public void deleteData(String id){
        context.getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI,
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{id});
    }
}
