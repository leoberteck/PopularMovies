package com.example.leonardo.popularmovies.entity;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractPaginatedResult<T> {
    private List<T> results = new ArrayList<>();
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int size() {
        return results.size();
    }

    public T get(int position) {
        return results.get(position);
    }

    public void add(T t) {
        if (!results.contains(t)) {
            results.add(t);
        }
    }

    public void addAll(@NonNull Collection<T> collection) {
        for (T t: collection) {
            add(t);
        }
    }

    public void addNextPage(AbstractPaginatedResult<T> page){
        addAll(page.getResults());
        setPage(page.getPage());
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
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
