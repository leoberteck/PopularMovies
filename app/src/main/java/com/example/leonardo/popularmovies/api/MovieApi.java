package com.example.leonardo.popularmovies.api;

import android.net.Uri;

import com.example.leonardo.popularmovies.entity.AbstractPaginatedResult;
import com.example.leonardo.popularmovies.entity.Movie;
import com.example.leonardo.popularmovies.entity.MoviePaginatedResult;
import com.example.leonardo.popularmovies.entity.ReviewPaginatedResult;
import com.example.leonardo.popularmovies.entity.TrailerResult;
import com.example.leonardo.popularmovies.enums.ImageSize;

import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MovieApi implements MovieAPIInterface {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = ApiConfig.getInstance().getApiKey();

    public MovieApi() {
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
    }

    @Override
    public MoviePaginatedResult listNowPlayng(int page, String locale) {
        return getPaginatedResult("now_playing", page, locale, MoviePaginatedResult.class);
    }

    @Override
    public MoviePaginatedResult listUpcoming(int page, String locale) {
        return getPaginatedResult("upcoming", page, locale, MoviePaginatedResult.class);
    }

    @Override
    public MoviePaginatedResult listTopRated(int page, String locale) {
        return getPaginatedResult("top_rated", page, locale, MoviePaginatedResult.class);
    }

    @Override
    public MoviePaginatedResult listPopular(int page, String locale) {
        return getPaginatedResult("popular", page, locale, MoviePaginatedResult.class);
    }

    @Override
    public ReviewPaginatedResult getReviews(long id, int page, String locale){
        return getPaginatedResultById(id, "reviews", page, locale, ReviewPaginatedResult.class);
    }

    @Override
    public Movie getDetails(long id, String locale) {
        return restTemplate.getForObject(
            getMovieApiBaseUriBuilder()
                .appendPath(String.valueOf(id))
                .appendQueryParameter("api_key", apiKey)
                .appendQueryParameter("language", locale)
                .build()
                .toString()
            , Movie.class);
    }

    @Override
    public String getImageUrl(String imageId, ImageSize imageSize) {
        return getImageApiBaseUriBuilder()
            .appendPath(imageSize.getValue())
            .appendEncodedPath(imageId)
            .build()
            .toString();
    }

    @Override
    public TrailerResult getTrailers(long id){
        return restTemplate.getForObject(
            getMovieApiBaseUriBuilder()
                .appendPath(String.valueOf(id))
                .appendPath("videos")
                .appendQueryParameter("api_key", apiKey)
                .build()
                .toString()
            , TrailerResult.class
        );
    }

    private <T, D extends AbstractPaginatedResult<T>> D getPaginatedResult(
        String path, int page, String locale, Class<D> dClass)
    {
        return restTemplate.getForObject(
            getMovieApiBaseUriBuilder()
                .appendPath(path)
                .appendQueryParameter("api_key", apiKey)
                .appendQueryParameter("language", locale)
                .appendQueryParameter("page", String.valueOf(page))
                .build()
                .toString()
            , dClass);
    }

    private <T, D extends AbstractPaginatedResult<T>> D getPaginatedResultById(
            long id, String path, int page, String locale, Class<D> dClass)
    {
        return restTemplate.getForObject(
            getMovieApiBaseUriBuilder()
                .appendPath(String.valueOf(id))
                .appendPath(path)
                .appendQueryParameter("api_key", apiKey)
                .appendQueryParameter("language", locale)
                .appendQueryParameter("page", String.valueOf(page))
                .build()
                .toString()
            , dClass);
    }

    private Uri.Builder getImageApiBaseUriBuilder(){
        return new Uri.Builder()
            .scheme("http")
            .authority("image.tmdb.org")
            .appendPath("t")
            .appendPath("p");
    }

    private Uri.Builder getMovieApiBaseUriBuilder(){
        return new Uri.Builder()
            .scheme("https")
            .authority("api.themoviedb.org")
            .appendPath("3")
            .appendPath("movie");
    }
}
