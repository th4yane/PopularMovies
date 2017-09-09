package com.thayane.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Thayane on 13/08/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder>{
    private String[] mTrailersData;
    private Context mContext;

    public TrailersAdapter(Context context) {
        mContext = context;
    }
    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailers_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapterViewHolder holder, int position) {
        String videoPath = mTrailersData[position];
        holder.trailerKey = videoPath;
        holder.mTrailerImageView.setImageResource(R.drawable.play);
        holder.mKeyTextView.setText(mContext.getString(R.string.trailer)+" "+ (position+1));

        /*String imageUrl = setImageUrl(posterPath); //build url

        //Load image into a ImageView
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.image)
                .error(R.drawable.image_error)
                .into(holder.mMoviePosterImageView);*/

        //holder.mMoviePosterImageView.setTag(id); //set tag with the id of the movie, to use later
    }

    @Override
    public int getItemCount() {
        if(mTrailersData == null){
            return 0;
        } else{
            return mTrailersData.length;
        }
    }

    public void setTrailersData(String[] trailersData) {
        mTrailersData = trailersData;
        notifyDataSetChanged();
    }

    public String buildMovieUrl(String key) {
        String movieUrl = "http://www.youtube.com/watch?v=";
        movieUrl += key;
        return movieUrl;
    }

    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mTrailerImageView;
        public final TextView mKeyTextView;
        public String trailerKey;

        public TrailersAdapterViewHolder(View view) {
            super(view);
            mTrailerImageView = (ImageView) view.findViewById(R.id.iv_trailer);
            mKeyTextView = (TextView) view.findViewById(R.id.tv_trailer_key);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String url = buildMovieUrl(trailerKey);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mContext.startActivity(intent);
        }

    }
}
