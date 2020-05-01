package com.example.popularmovies;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private int movieId;
    final private ImageView movieImageView;
    final private ImageView movieFavoriteImageView;
    final private MoviesAdapter.MovieItemClickListener mMovieItemClickListener;

    MoviesAdapterViewHolder(@NonNull View itemView, MoviesAdapter.MovieItemClickListener movieItemClickListener) {
        super(itemView);
        mMovieItemClickListener = movieItemClickListener;
        movieImageView = itemView.findViewById(R.id.iv_movie_poster);
        movieFavoriteImageView = itemView.findViewById(R.id.iv_movie_favorite);
        itemView.setOnClickListener(this);
    }

    void loadImage(Uri uri) {
        Picasso.get().load(uri)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .fit().centerCrop()
                .into(movieImageView);
    }

    void setFavoriteImage(int drawableID) {
        movieFavoriteImageView.setImageResource(drawableID);
    }

    boolean getFavouriteImageStatus() {
        return this.movieFavoriteImageView.getDrawable() == null;
    }

    void setMovieId(int id) {
        this.movieId = id;
    }

    @Override
    public void onClick(View v) {
        mMovieItemClickListener.onMovieItemClick(movieId);
    }
}
