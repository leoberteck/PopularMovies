package com.example.leonardo.popularmovies.mvp;

import android.databinding.Bindable;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.Observable;
import android.support.v7.widget.RecyclerView;

import com.example.leonardo.popularmovies.adapters.MovieGridAdapter;
import com.example.leonardo.popularmovies.enums.MovieSort;

public interface MovieListMvp {
    interface MovieListActivityInterface{

    }

    @BindingMethods(value = {
        @BindingMethod(type = RecyclerView.class, attribute = "android:adapter", method = "setAdapter")
    })
    interface MovieListPresenterInterface extends Observable {
        @Bindable
        MovieGridAdapter getAdapter();
        void changeSort(MovieSort sort, String locale);
        void loadNextPage();
    }
}
