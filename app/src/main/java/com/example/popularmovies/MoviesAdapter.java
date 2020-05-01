package com.example.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapterViewHolder> {
    private List<Movie> mMovieList;

    final private MovieItemClickListener mMovieItemClickListener;

    MoviesAdapter(MovieItemClickListener onClickListener) {
        mMovieItemClickListener = onClickListener;
    }

    void setMovieList(List<Movie> movies) {
        mMovieList = movies;
    }

    public interface MovieItemClickListener {
        void onMovieItemClick(int movieId);
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        boolean attachToParentImmediately = false;
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, attachToParentImmediately);
        return new MoviesAdapterViewHolder(view, mMovieItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);
        Uri imageUri = NetworkUtils.getImageURL(movie.getPoster());
        holder.loadImage(imageUri);
        holder.setMovieId(movie.getId());
        holder.setFavoriteImage(android.R.color.transparent);
        if (movie.getFavorite()) {
            holder.setFavoriteImage(R.drawable.ic_stars_pink);
        }
    }

    @Override
    public int getItemCount() {
        if (mMovieList != null) {
            return mMovieList.size();
        }
        return 0;
    }

}
