package com.example.leonardo.udacitypopularmovies.entity;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class MoviePaginatedResult implements Collection<Movie> {

    private List<Movie> results = new ArrayList<>();
    private int page;
    private int totalResults;
    private int totalPages;

    @Override
    public int size() {
        return results.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return results.contains(o);
    }

    @NonNull
    @Override
    public Iterator<Movie> iterator() {
        return results.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return results.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] ts) {
        return results.toArray(ts);
    }

    @Override
    public boolean add(Movie movie) {
        return results.add(movie);
    }

    @Override
    public boolean remove(Object o) {
        return results.remove(o);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return results.containsAll(collection);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Movie> collection) {
        return results.addAll(collection);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return results.removeAll(collection);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return results.retainAll(collection);
    }

    @Override
    public void clear() {
        results.clear();
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
