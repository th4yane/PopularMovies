package com.thayane.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thayane.popularmovies.utilities.Utility;

/**
 * Created by Thayane on 19/08/2017.
 */

public class MovieCursorAdapter extends CursorRecyclerViewAdapter<MoviesAdapter.MoviesAdapterViewHolder>
        implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private Context mContext;



    @Override
    public void clickHandler(String id) {

    }


    public MovieCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext = context;
    }


    @Override
    public MoviesAdapter.MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MoviesAdapter.MoviesAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return getCursor().getCount();
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MoviesAdapterViewHolder viewHolder, Cursor cursor) {
            String posterPath = cursor.getString(cursor.getColumnIndex("poster"));
            String id = cursor.getString(cursor.getColumnIndex("id"));

            String imageUrl = Utility.buildImageUrl(posterPath); //build url

            //Load image into a ImageView
            Picasso.with(mContext)
                    .load(imageUrl)
                    .placeholder(R.drawable.image)
                    .error(R.drawable.image_error)
                    .into(viewHolder.mMoviePosterImageView);
            viewHolder.mMoviePosterImageView.setTag(id);
    }
}
