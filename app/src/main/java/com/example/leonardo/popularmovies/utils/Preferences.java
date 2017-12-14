package com.example.leonardo.popularmovies.utils;

import android.content.SharedPreferences;

import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.enums.MovieSort;

public final class Preferences {

    private SharedPreferences sharedPreferences;
    private ResourceUtils resourceUtils;

    public Preferences(SharedPreferences sharedPreferences, ResourceUtils resourceUtils) {
        this.sharedPreferences = sharedPreferences;
        this.resourceUtils = resourceUtils;
    }

    public MovieSort getMovieSort() {
        return MovieSort.valueOf(Integer.valueOf(sharedPreferences.getString(resourceUtils.getString(R.string.movie_sort_key), "1")));
    }

    public void setMovieSort(MovieSort movieSort) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(resourceUtils.getString(R.string.movie_sort_key), movieSort.getValue());
        editor.apply();
    }
}
