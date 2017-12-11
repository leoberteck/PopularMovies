package com.example.leonardo.udacitypopularmovies.utils;

import android.content.SharedPreferences;

import com.example.leonardo.udacitypopularmovies.enums.MovieSort;

public final class Preferences {

    private final String MOVIE_SORT_KEY = "MOVIE_SORT_KEY";

    private SharedPreferences sharedPreferences;

    public Preferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public MovieSort getMovieSort() {
        return MovieSort.valueOf(sharedPreferences.getInt(MOVIE_SORT_KEY, 1));
    }

    public void setMovieSort(MovieSort movieSort) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MOVIE_SORT_KEY, movieSort.getValue());
        editor.apply();
    }
}
