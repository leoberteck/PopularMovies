package com.example.leonardo.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.api.YoutubeApiInterface;
import com.example.leonardo.popularmovies.entity.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {

    @NonNull
    private final List<Trailer> trailerList;
    @NonNull
    private final YoutubeApiInterface youtubeApiInterface;
    @NonNull
    private final ItemClickListener itemClickListener;

    public MovieTrailerAdapter(
            @NonNull List<Trailer> trailerList
            , @NonNull YoutubeApiInterface youtubeApiInterface
            , @NonNull ItemClickListener itemClickListener)
    {
        this.trailerList = trailerList;
        this.youtubeApiInterface = youtubeApiInterface;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_trailer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(trailerList.get(position));
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public interface ItemClickListener {
        void onItemClick(Trailer trailer);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Trailer trailer;
        private final ImageView thumbImageView;
        private final Context context;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            context = itemView.getContext();
            thumbImageView = itemView.findViewById(R.id.imageView_trailer_thumb);
        }

        void bind(Trailer trailer){
            this.trailer = trailer;
            Picasso.with(context)
                .load(youtubeApiInterface.getVideoThumbnailUrl(
                    trailer.getKey()
                ))
                .into(thumbImageView);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(trailer);
        }
    }
}
