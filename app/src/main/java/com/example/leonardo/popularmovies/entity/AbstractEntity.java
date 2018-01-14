package com.example.leonardo.popularmovies.entity;


public class AbstractEntity implements BaseEntity {

    private long databaseId;

    @Override
    public long getDatabaseId() {
        return databaseId;
    }

    @Override
    public void setDatabaseId(long databaseId) {
        this.databaseId = databaseId;
    }
}
