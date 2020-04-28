package com.example.popularmovies;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final private TextView trailerTitleTextView;
    final private ImageView trailerImageView;
    final private TrailersAdapter.TrailerItemClickListener mTrailerItemClickListener;

    public TrailersAdapterViewHolder(@NonNull View itemView, TrailersAdapter.TrailerItemClickListener trailerItemClickListener) {
        super(itemView);

        trailerTitleTextView = itemView.findViewById(R.id.tv_trailer_title);
        trailerImageView = itemView.findViewById(R.id.iv_trailer_thumbnail);
        mTrailerItemClickListener = trailerItemClickListener;
        itemView.setOnClickListener(this);
    }

    void setTrailerTitleTextView(String title) {
        trailerTitleTextView.setText(title);
    }

    void loadImage(Uri uri) {
        Picasso.get().load(uri)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .fit().centerCrop()
                .into(trailerImageView, new Callback() {
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
        mTrailerItemClickListener.onTrailerItemClick(clickedPosition);
    }
}
