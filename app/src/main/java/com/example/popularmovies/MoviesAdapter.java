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

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>  {
    private List<Movie> mMovieList;

    final private MovieItemClickListener mMovieItemClickListener;

    MoviesAdapter(MovieItemClickListener onClickListener) {
        mMovieItemClickListener = onClickListener;
    }

    void setMovieList(List<Movie> movies) {
        mMovieList = movies;
    }

    public interface MovieItemClickListener {
        void onMovieItemClick(int clickedMovieIndex);
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        boolean attachToParentImmediately = false;
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item,parent, attachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder holder, int position) {
        Log.d("POSITION", position+"");
        Uri imageUri = NetworkUtils.getImageURL(mMovieList.get(position).getImage());
        Log.d("IMAGE URI", imageUri.toString());
        holder.loadImage(imageUri);
    }

    @Override
    public int getItemCount() {
        if ( mMovieList != null ) {
            return mMovieList.size();
        }
        return 0;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final private ImageView movieImageView;

        MoviesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
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
}
