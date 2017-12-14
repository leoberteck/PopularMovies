package com.example.leonardo.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.api.MovieAPIInterface;
import com.example.leonardo.popularmovies.entity.Movie;
import com.example.leonardo.popularmovies.entity.MoviePaginatedResult;
import com.example.leonardo.popularmovies.enums.ImageSize;
import com.example.leonardo.popularmovies.enums.MovieSort;
import com.squareup.picasso.Picasso;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> {
    private MoviePaginatedResult dataSet;
    private MovieAPIInterface movieApi;
    private MovieSort movieSort;
    private String locale;
    private ItemClickListener itemClickListener;

    public MovieGridAdapter(MovieAPIInterface movieApi, MovieSort movieSort, String locale, ItemClickListener itemClickListener) {
        this.movieApi = movieApi;
        this.movieSort = movieSort;
        this.locale = locale;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet != null ? dataSet.size() : 0;
    }

    public void addPage(MoviePaginatedResult movies) {
        if(dataSet == null){
            dataSet = movies;
        } else if(dataSet.getPage() < movies.getPage()){
            dataSet.addAll(movies.getResults());
            dataSet.setPage(movies.getPage());
        }
        notifyDataSetChanged();
    }

    public int getCurrentPage(){
        return dataSet != null ? dataSet.getPage() : 0;
    }

    public int getTotalPages(){
        return dataSet != null ? dataSet.getTotalPages() : 0;
    }

    public MovieSort getMovieSort() {
        return movieSort;
    }

    public String getLocale() {
        return locale;
    }

    public interface ItemClickListener{
        void onItemClick(Movie movie);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context context;
        private ImageView imageView;
        private TextView textView;
        private Movie movie;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            imageView = itemView.findViewById(R.id.image_view_movie_poster);
            textView = itemView.findViewById(R.id.textView_movie_title);
            itemView.setOnClickListener(this);
        }

        Context getContext() {
            return context;
        }

        ImageView getImageView() {
            return imageView;
        }

        TextView getTextView() {
            return textView;
        }

        void bind(Movie movie){
            this.movie = movie;
            getTextView().setText(movie.getTitle());
            Picasso
                .with(getContext())
                .load(movieApi.getImageUrl(movie.getPosterPath(), ImageSize.W185))
                .into(getImageView());
        }

        @Override
        public void onClick(View view) {
            if(itemClickListener != null){
                itemClickListener.onItemClick(movie);
            }
        }
    }
}
