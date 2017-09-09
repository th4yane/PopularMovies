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

import org.w3c.dom.Text;

/**
 * Created by Thayane on 26/08/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder>{
    private ReviewData[] mReviewsData;
    private Context mContext;

    public ReviewsAdapter(Context context) {
        mContext = context;
    }
    @Override
    public ReviewsAdapter.ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ReviewsAdapter.ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewsAdapterViewHolder holder, int position) {
        String author = mReviewsData[position].author;
        String review = mReviewsData[position].review;

        holder.mAuthorTextView.setText(author);
        holder.mContentTextView.setText(review);
    }

    @Override
    public int getItemCount() {
        if(mReviewsData == null){
            return 0;
        } else{
            return mReviewsData.length;
        }
    }

    public void setReviewsData(ReviewData[] reviewsData) {
        mReviewsData = reviewsData;
        notifyDataSetChanged();
    }


    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mAuthorTextView, mContentTextView;

        public ReviewsAdapterViewHolder(View view) {
            super(view);
            mAuthorTextView = (TextView) view.findViewById(R.id.tv_author_review);
            mContentTextView = (TextView) view.findViewById(R.id.tv_content_review);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

    }
}
