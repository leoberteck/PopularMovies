package com.example.leonardo.popularmovies.data.dao;

import com.example.leonardo.popularmovies.data.contract.BaseContractEntry;
import com.example.leonardo.popularmovies.entity.BaseEntity;

import java.util.List;

/**
 * Created by leonardo on 13/01/18.
 */

public interface AbstractDaoInterface<T extends BaseEntity, U extends BaseContractEntry<T>> {
    void query(OnTaskFinishListener<List<T>> listener);

    void query(String selection, String[] selectionArgs, OnTaskFinishListener<List<T>> listener);

    void findOneById(long id, OnTaskFinishListener<T> listener);

    void findOne(String selection, String[] selectionArgs, OnTaskFinishListener<T> listener);

    void insert(T entity, OnTaskFinishListener<T> listener);

    void delete(long id, OnTaskFinishListener<Integer> listener);

    void delete(T entity, OnTaskFinishListener<Integer> listener);

    void update(T entity, OnTaskFinishListener<Integer> listener);

    interface OnTaskFinishListener<T>{
        void onTaskFinish(Exception ex, T result);
    }
}
