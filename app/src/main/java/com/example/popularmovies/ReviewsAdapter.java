package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.model.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapterViewHolder> {
    private List<Review> mReviewList;

    public void setReviewList(List<Review> reviews) {
        mReviewList = reviews;
    }

    @NonNull
    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        boolean attachToParentImmediately = false;
        View view = LayoutInflater.from(context).inflate(R.layout.review_list_item, parent, attachToParentImmediately);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapterViewHolder holder, int position) {
        holder.setAuthor(mReviewList.get(position).getAuthor());
        holder.setContent(mReviewList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if( mReviewList != null ) {
            return mReviewList.size();
        }
        return 0;
    }
}
