package com.example.leonardo.popularmovies.utils;


import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;

public class ResourceUtils {

    private Context context;

    public ResourceUtils(Context context) {
        this.context = context;
    }

    public String getString(@StringRes int resId){
        return context.getString(resId);
    }
    public String[] getStringArray(@ArrayRes int resId){
        return context.getResources().getStringArray(resId);
    }
}
