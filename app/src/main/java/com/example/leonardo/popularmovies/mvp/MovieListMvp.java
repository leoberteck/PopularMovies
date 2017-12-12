package com.example.leonardo.popularmovies.mvp;

import android.databinding.Bindable;
import android.databinding.Observable;

import com.example.leonardo.popularmovies.adapters.MovieGridAdapter;
import com.example.leonardo.popularmovies.enums.MovieSort;

public interface MovieListMvp {
    interface MovieListActivityInterface{

    }

    interface MovieListPresenterInterface extends Observable {
        @Bindable
        MovieGridAdapter getAdapter();

        void changeSort(MovieSort sort, String locale);

        void loadNextPage();
    }
}
