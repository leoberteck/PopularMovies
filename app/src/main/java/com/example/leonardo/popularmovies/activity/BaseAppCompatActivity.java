package com.example.leonardo.popularmovies.activity;

import android.content.Context;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.mvp.BaseActivityInterface;

import java.util.Locale;

public abstract class BaseAppCompatActivity extends AppCompatActivity implements BaseActivityInterface {

    protected Locale getCurrentLocale(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return context.getResources().getConfiguration().getLocales().get(0);
        } else{
            return context.getResources().getConfiguration().locale;
        }
    }

    protected String getCurrentLanguage(){
        return getCurrentLocale(this).getLanguage();
    }

    @Override
    public void showDialog(@StringRes int title, @StringRes int message){
        new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.ok, null)
            .show();
    }

    @Override
    public void showErrorMessage(@StringRes int title, @StringRes int message, Exception e){
        new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(
                getString(message)
                + " ."
                + getString(R.string.details)
                + ": "
                + e.getLocalizedMessage()
            )
            .setPositiveButton(R.string.ok, null)
            .show();
    }
}
