package com.example.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.utilities.NetworkUtils;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapterViewHolder>{
    private List<Trailer> mTrailersList;

    void setTrailerList(List<Trailer> trailers) {
        mTrailersList = trailers;
    }

    @NonNull
    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        boolean attachToParentImmediately = false;
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_list_item, parent, attachToParentImmediately);
        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersAdapterViewHolder holder, int position) {
        Log.d("POSITION", position+"");
        Uri imageUri = NetworkUtils.getYouTubeImageURL(mTrailersList.get(position).getKey());
        Log.d("IMAGE URI", imageUri.toString());
        holder.loadImage(imageUri);
    }

    @Override
    public int getItemCount() {
        if ( mTrailersList != null ) {
            return mTrailersList.size();
        }
        return 0;
    }
}
