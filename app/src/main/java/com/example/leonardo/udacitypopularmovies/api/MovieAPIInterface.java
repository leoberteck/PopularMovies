package com.example.leonardo.udacitypopularmovies.api;


import com.example.leonardo.udacitypopularmovies.entity.Movie;
import com.example.leonardo.udacitypopularmovies.entity.MoviePaginatedResult;
import com.example.leonardo.udacitypopularmovies.enums.ImageSize;

/**
 * Created by leonardo on 07/12/17.
 * Interface that abstracts connection with themoviedb.org API
 * This api provides the user with the ability to list and filter movies
 * as well as the ability to search for the details of a specific movie.
 */

public interface MovieAPIInterface {
    MoviePaginatedResult listNowPlayng(int page, String locale);
    MoviePaginatedResult listUpcoming(int page, String locale);
    MoviePaginatedResult listTopRated(int page, String locale);
    Movie getDetails(int id, String locale);
    String getImageUrl(String imageId, ImageSize imageSize);
}