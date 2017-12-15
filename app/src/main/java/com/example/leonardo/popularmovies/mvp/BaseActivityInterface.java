package com.example.leonardo.popularmovies.mvp;

import android.support.annotation.StringRes;

/**
 * Created by leonardo on 15/12/17.
 */

public interface BaseActivityInterface {
    void showDialog(@StringRes int title, @StringRes int message);

    void showErrorMessage(@StringRes int title, @StringRes int message, Exception e);
}
