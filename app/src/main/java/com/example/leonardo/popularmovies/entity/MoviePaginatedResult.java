package com.example.leonardo.popularmovies.entity;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MoviePaginatedResult {

    private List<Movie> results = new ArrayList<>();
    private int page;
    private int totalResults;
    private int totalPages;

    //@Override
    public int size() {
        return results.size();
    }

    //@Override
    public boolean isEmpty() {
        return size() == 0;
    }

    //@Override
    public boolean contains(Object o) {
        return results.contains(o);
    }

    @NonNull
    //@Override
    public Iterator<Movie> iterator() {
        return results.iterator();
    }

    @NonNull
    //@Override
    public Object[] toArray() {
        return results.toArray();
    }

    @NonNull
    //@Override
    public <T> T[] toArray(@NonNull T[] ts) {
        return results.toArray(ts);
    }

    //@Override
    public boolean add(Movie movie) {
        return results.add(movie);
    }

    //@Override
    public boolean remove(Object o) {
        return results.remove(o);
    }

    //@Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return results.containsAll(collection);
    }

    //@Override
    public boolean addAll(@NonNull Collection<? extends Movie> collection) {
        return results.addAll(collection);
    }

    //@Override
    public boolean addAll(int i, @NonNull Collection<? extends Movie> collection) {
        return results.addAll(i, collection);
    }

    //@Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return results.removeAll(collection);
    }

    //@Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return results.retainAll(collection);
    }

    //@Override
    public void clear() {
        results.clear();
    }

    //@Override
    public Movie get(int i) {
        return results.get(i);
    }

    //@Override
    public Movie set(int i, Movie movie) {
        return results.set(i, movie);
    }

    //@Override
    public void add(int i, Movie movie) {
        results.add(i, movie);
    }

    //@Override
    public Movie remove(int i) {
        return results.remove(i);
    }

    //@Override
    public int indexOf(Object o) {
        return results.indexOf(o);
    }

    //@Override
    public int lastIndexOf(Object o) {
        return results.lastIndexOf(o);
    }

    @NonNull
    //@Override
    public ListIterator<Movie> listIterator() {
        return results.listIterator();
    }

    @NonNull
    //@Override
    public ListIterator<Movie> listIterator(int i) {
        return results.listIterator(i);
    }

    @NonNull
    //@Override
    public List<Movie> subList(int i, int i1) {
        return results.subList(i, i1);
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
