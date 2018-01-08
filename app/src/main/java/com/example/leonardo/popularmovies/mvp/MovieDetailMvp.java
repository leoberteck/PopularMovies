package com.example.leonardo.popularmovies.mvp;

import android.databinding.Bindable;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.Observable;
import android.support.v7.widget.RecyclerView;

public interface MovieDetailMvp {

    interface MovieDetailActivityInterface extends BaseActivityInterface{
        void loadMoviePoster(String uri);
        void openTrailer(String videoThumbnailUrl);
    }

    @BindingMethods(value = {
        @BindingMethod(type = RecyclerView.class, attribute = "android:adapter", method = "setAdapter")
    })
    interface MovieDetailPresenterInterface extends
        MovieInfoTitle
        , MovieInfoDetail
        , MovieInfoOverview
        , MovieInfoTrailers
        , MovieInfoReviews {

        void setMovie(long movieId, String locale);

        void setMovieDetailActivity(MovieDetailMvp.MovieDetailActivityInterface movieDetailActivity);

        void loadReviews(long id, String locale);

        void loadTrailers(long id);
    }

    interface MovieInfoTitle extends Observable{
        @Bindable
        String getTitle();
        @Bindable
        String getOriginalTitle();
    }

    interface MovieInfoDetail extends Observable{
        @Bindable
        String getReleaseDate();
        @Bindable
        String getDuration();
        @Bindable
        String getScore();
    }

    interface MovieInfoOverview extends Observable {
        @Bindable
        String getOverview();
    }

    interface MovieInfoTrailers extends Observable {
        @Bindable
        RecyclerView.Adapter getTrailerAdapter();
        @Bindable
        int getTrailersVisibility();
        @Bindable
        int getTrailersErrorVisibility();
    }

    interface MovieInfoReviews extends Observable {
        @Bindable
        RecyclerView.Adapter getReviewsAdapter();
        @Bindable
        int getReviewsVisibility();
        @Bindable
        int getReviewsErrorVisibility();
    }
}
