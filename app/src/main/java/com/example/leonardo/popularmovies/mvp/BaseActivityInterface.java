package com.example.leonardo.popularmovies.mvp;

import android.support.annotation.StringRes;

public interface BaseActivityInterface {
    void showDialog(@StringRes int title, @StringRes int message);

    void showErrorMessage(@StringRes int title, @StringRes int message, Exception e);
}
