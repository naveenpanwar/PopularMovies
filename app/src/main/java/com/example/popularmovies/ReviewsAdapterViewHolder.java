package com.example.popularmovies;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
    final private TextView authorTextView;
    final private TextView contentTextView;

    public ReviewsAdapterViewHolder(@NonNull View reviewView) {
        super(reviewView);
        authorTextView = reviewView.findViewById(R.id.tv_review_author);
        contentTextView = reviewView.findViewById(R.id.tv_review_content);
    }

    public void setAuthor(String author) {
        authorTextView.setText(author);
    }

    public void setContent(String content) {
        contentTextView.setText(content);
    }
}
