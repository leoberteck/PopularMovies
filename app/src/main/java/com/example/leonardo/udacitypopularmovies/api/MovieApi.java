package com.example.leonardo.udacitypopularmovies.api;

import android.net.Uri;

import com.example.leonardo.udacitypopularmovies.entity.Movie;
import com.example.leonardo.udacitypopularmovies.entity.MoviePaginatedResult;
import com.example.leonardo.udacitypopularmovies.enums.ImageSize;

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
        return getPaginatedResult("now_playing", page, locale);
    }

    @Override
    public MoviePaginatedResult listUpcoming(int page, String locale) {
        return getPaginatedResult("upcoming", page, locale);
    }

    @Override
    public MoviePaginatedResult listTopRated(int page, String locale) {
        return getPaginatedResult("top_rated", page, locale);
    }

    @Override
    public Movie getDetails(int id, String locale) {
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
            .appendPath(imageId)
            .build()
            .toString();
    }

    private MoviePaginatedResult getPaginatedResult(String path, int page, String locale){
        return restTemplate.getForObject(
            getMovieApiBaseUriBuilder()
                .appendPath(path)
                .appendQueryParameter("apy_key", apiKey)
                .appendQueryParameter("language", locale)
                .appendQueryParameter("page", String.valueOf(page))
                .build()
                .toString()
        , MoviePaginatedResult.class);
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
