package com.example.leonardo.udacitypopularmovies.mvp;

import android.databinding.Observable;

public interface MovieListMvp {
    interface MovieListActivityInterface{

    }

    interface MovieListPresenterInterface extends Observable {
        void showNowPlaying();
        void showUpcoming();
        void showTopRated();
    }
}
