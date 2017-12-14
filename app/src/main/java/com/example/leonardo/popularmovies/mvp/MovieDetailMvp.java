package com.example.leonardo.popularmovies.mvp;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.net.Uri;

public interface MovieDetailMvp {

    interface MovieDetailActivityInterface {
        void loadMoviePoster(Uri uri);
    }

    interface MovieDetailPresenterInterface extends Observable {
        @Bindable
        String getTitle();
        @Bindable
        String getOriginalTitle();
        @Bindable
        String getReleaseDate();
        @Bindable
        String getDuration();
        @Bindable
        String getScore();
        @Bindable
        String getOverview();

        void setMovie(long movieId);
        void setMovieDetailActivity(MovieListMvp.MovieListActivityInterface movieDetailActivity);
    }
}
