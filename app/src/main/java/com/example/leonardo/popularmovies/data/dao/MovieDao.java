package com.example.leonardo.popularmovies.data.dao;

import android.content.ContentResolver;
import android.support.annotation.NonNull;

import com.example.leonardo.popularmovies.data.contract.MovieContract.MovieEntry;
import com.example.leonardo.popularmovies.entity.Movie;


public class MovieDao extends AbstractDao<Movie, MovieEntry> {
    public MovieDao(@NonNull ContentResolver contentResolver) {
        super(MovieEntry.getInstance(), contentResolver);
    }

    public void findByMovieId(long movieId, OnTaskFinishListener<Movie> listener){
        findOne(MovieEntry.MOVIE_ID + "=?", new String[]{String.valueOf(movieId)}, listener);
    }

    private MovieEntry getContract(){
        return (MovieEntry)contract;
    }
}
