package com.example.leonardo.popularmovies.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by leonardo on 07/12/17.
 * Representation of the movie returned by the themoviedb.org API
 */

public class Movie extends AbstractEntity {
    private long movieId;
    private String title;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("original_language")
    private String originalLanguage;
    private String overview;
    @SerializedName("release_date")
    private Date releaseDate;
    private int runtime;
    @SerializedName("vote_average")
    private double voteAverage;

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return getMovieId() == movie.getMovieId() && getTitle().equals(movie.getTitle());
    }

    @Override
    public int hashCode() {
        int result = (int) (getMovieId() ^ (getMovieId() >>> 32));
        result = 31 * result + getTitle().hashCode();
        return result;
    }
}

