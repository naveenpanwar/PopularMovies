package com.example.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.utilities.NetworkUtils;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapterViewHolder>{
    private List<Trailer> mTrailersList;

    final private TrailerItemClickListener mTrailerItemClickListener;

    TrailersAdapter(TrailerItemClickListener trailerItemClickListener) {
        mTrailerItemClickListener = trailerItemClickListener;
    }

    public interface TrailerItemClickListener {
        void onTrailerItemClick(int clickedTrailerIndex);
    }

    void setTrailerList(List<Trailer> trailers) {
        mTrailersList = trailers;
    }

    @NonNull
    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        boolean attachToParentImmediately = false;
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_list_item, parent, attachToParentImmediately);
        return new TrailersAdapterViewHolder(view, mTrailerItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersAdapterViewHolder holder, int position) {
        Log.d("POSITION", position+"");
        Trailer trailer = mTrailersList.get(position);
        Uri imageUri = NetworkUtils.getYouTubeImageURL(trailer.getKey());
        Log.d("IMAGE URI", imageUri.toString());
        holder.loadImage(imageUri);
        holder.setTrailerTitleTextView(trailer.getName());
    }

    @Override
    public int getItemCount() {
        if ( mTrailersList != null ) {
            return mTrailersList.size();
        }
        return 0;
    }
}
