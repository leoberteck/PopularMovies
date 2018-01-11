package com.example.leonardo.popularmovies;

import android.app.Application;

import com.example.leonardo.popularmovies.dao.DbHelper;

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    private DbHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dbHelper = new DbHelper(this);
    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }
}
