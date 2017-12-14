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

    public MovieGridAdapter(MovieAPIInterface movieApi, MovieSort movieSort, String locale) {
        this.movieApi = movieApi;
        this.movieSort = movieSort;
        this.locale = locale;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = dataSet.get(position);
        holder.getTextView().setText(movie.getTitle());
        Picasso
            .with(holder.getContext())
            .load(movieApi.getImageUrl(movie.getPosterPath(), ImageSize.W185))
            .into(holder.getImageView());
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        private Context context;
        private ImageView imageView;
        private TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            imageView = itemView.findViewById(R.id.image_view_movie_poster);
            textView = itemView.findViewById(R.id.textView_movie_title);
        }

        public Context getContext() {
            return context;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
