package com.example.leonardo.popularmovies.mvp;

import android.databinding.Bindable;
import android.databinding.Observable;

public interface MovieDetailMvp {

    interface MovieDetailActivityInterface {
        void loadMoviePoster(String uri);
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

        void setMovie(long movieId, String locale);

        void setMovieDetailActivity(MovieDetailMvp.MovieDetailActivityInterface movieDetailActivity);
    }
}
