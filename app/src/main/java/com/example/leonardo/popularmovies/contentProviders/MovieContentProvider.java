package com.example.leonardo.popularmovies.contentProviders;

import android.content.UriMatcher;

import com.example.leonardo.popularmovies.dao.contract.BaseContractEntry;
import com.example.leonardo.popularmovies.dao.contract.MovieContract;

public class MovieContentProvider extends AbstractContentProvider {

    private static final int MOVIES = 100;
    private static final int MOVIES_BY_ID = 101;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.MOVIE_PATH, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.MOVIE_PATH + "/#", MOVIES_BY_ID);
    }

    @Override
    protected UriMatcher getUriMatcher() {
        return uriMatcher;
    }

    @Override
    protected int getUriContentMatchId() {
        return MOVIES;
    }

    @Override
    protected int getUriContentByIdMatchId() {
        return MOVIES_BY_ID;
    }

    @Override
    protected BaseContractEntry getContractEntry() {
        return MovieContract.MovieEntry.getInstance();
    }
}
