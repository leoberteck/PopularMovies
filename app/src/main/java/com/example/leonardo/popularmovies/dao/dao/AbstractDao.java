package com.example.leonardo.popularmovies.dao.dao;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.example.leonardo.popularmovies.dao.contract.BaseContractEntry;
import com.example.leonardo.popularmovies.entity.BaseEntity;

import java.util.List;


public abstract class AbstractDao<T extends BaseEntity, U extends BaseContractEntry, K> implements DaoInterface<T, U, K> {

    @NonNull
    private SQLiteOpenHelper openHelper;
    @NonNull
    private U contract;

    public AbstractDao(@NonNull SQLiteOpenHelper openHelper, @NonNull U contract) {
        this.openHelper = openHelper;
        this.contract = contract;
    }

    @Override
    public T get(K id) {
        return null;
    }

    @Override
    public List<T> query() {
        return null;
    }

    @Override
    public List<T> query(String selection, String[] selectionArgs) {
        return null;
    }

    @Override
    public long insert(T entity) {
        return 0;
    }

    @Override
    public int delete(K id) {
        return 0;
    }

    @Override
    public int delete(T entity) {
        return 0;
    }

    @Override
    public int update(T entity) {
        return 0;
    }
}
