package com.thayane.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by Thayane on 10/02/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private MovieData[] mMoviesData;
    private Context mContext;

    final private MoviesAdapterOnClickHandler mClickHandler;


    interface MoviesAdapterOnClickHandler{
        void clickHandler(String id);
    }

    public MoviesAdapter(MoviesAdapterOnClickHandler moviesAdapterOnClickHandler, Context context) {
        mClickHandler = moviesAdapterOnClickHandler;
        mContext = context;
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(mMoviesData == null){
            return 0;
        } else{
            return mMoviesData.length;
        }
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        String posterPath = mMoviesData[position].imagePath;
        String id = mMoviesData[position].id;

        String imageUrl = setImageUrl(posterPath, "w500"); //build url

        Picasso.with(mContext).load(imageUrl).into(holder.mMoviePosterImageView); //load image into imageview
        holder.mMoviePosterImageView.setTag(id); //set tag with the id of the movie, to use later
    }

    public void setMoviesData(MovieData[] moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }

    /* This method builds the url of the image */
    public String setImageUrl (String posterPath, String size){
        String imageUrl = "http://image.tmdb.org/t/p/";
        imageUrl += size + posterPath;
        return imageUrl;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMoviePosterImageView;
        public String movieId;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mMoviePosterImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            movieId = String.valueOf(mMoviePosterImageView.getTag());
            mClickHandler.clickHandler(movieId);
        }

    }
}
