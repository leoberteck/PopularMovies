package com.example.leonardo.popularmovies.entity;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MoviePaginatedResult {

    private List<Movie> results = new ArrayList<>();
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int size() {
        return results.size();
    }

    public Movie get(int position) {
        return results.get(position);
    }

    public void add(Movie movie) {
        if (!results.contains(movie)) {
            results.add(movie);
        }
    }

    public void addAll(@NonNull Collection<? extends Movie> collection) {
        for (Movie movie: collection) {
            add(movie);
        }
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}
