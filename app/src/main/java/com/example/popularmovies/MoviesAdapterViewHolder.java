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

    final private ImageView movieImageView;
    final private MoviesAdapter.MovieItemClickListener mMovieItemClickListener;

    MoviesAdapterViewHolder(@NonNull View itemView, MoviesAdapter.MovieItemClickListener movieItemClickListener) {
        super(itemView);
        mMovieItemClickListener = movieItemClickListener;
        movieImageView = itemView.findViewById(R.id.iv_movie_poster);
        itemView.setOnClickListener(this);
    }

    void loadImage(Uri uri) {
        Picasso.get().load(uri)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .fit().centerCrop()
                .into(movieImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("PICASSO DEBUG", "Everything is ok");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("PICASSO DEBUG", "Error on loading image.");
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int clickedPosition = getAdapterPosition();
        mMovieItemClickListener.onMovieItemClick(clickedPosition);
    }
}
