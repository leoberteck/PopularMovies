package com.example.leonardo.popularmovies.utils;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.enums.MovieSort;

public final class Preferences {

    @NonNull
    private final SharedPreferences sharedPreferences;
    @NonNull
    private final ResourceUtils resourceUtils;

    public Preferences(@NonNull SharedPreferences sharedPreferences, @NonNull ResourceUtils resourceUtils) {
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

    public int getAppThemeDayNightMode(){
        String appTheme = sharedPreferences.getString(resourceUtils.getString(R.string.app_theme_key), "1");
        int mode;
        switch (appTheme) {
            case "1":
                mode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case "2":
                mode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            default:
                mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
        }
        return mode;
    }
}
