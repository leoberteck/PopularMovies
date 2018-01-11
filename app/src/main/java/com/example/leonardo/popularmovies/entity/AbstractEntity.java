package com.example.leonardo.popularmovies.entity;


public class AbstractEntity implements BaseEntity {

    private long id;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }
}
