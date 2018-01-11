package com.example.leonardo.popularmovies.dao.dao;


import com.example.leonardo.popularmovies.dao.contract.BaseContractEntry;
import com.example.leonardo.popularmovies.entity.BaseEntity;

import java.util.List;

/**
 * This class defines the methods to put and retrieve data from
 * a SQLite database
 * @param <T> The type of the entity to be accessed in the database
 * @param <U> The type of the contract class that will dictate how to
 *           process the entities returned in the cursors.
 * @param <K> The type of the class uses as primary key in the entity
 */
public interface DaoInterface<T extends BaseEntity, U extends BaseContractEntry, K> {

    T get(K id);
    List<T> query();
    //List<T> query(String orderBy);
    List<T> query(String selection, String[] selectionArgs);
    //List<T> query(String selection, String[] selectionArgs, String orderBy);
    //List<T> query(String selection, String[] selectionArgs, String groupBy, String orderBy);
    //List<T> query(String selection, String[] selectionArgs, String groupBy, String having, String orderBy);
    //List<T> query(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);
    //List<T> query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);

    long insert(T entity);
    int delete(K id);
    int delete(T entity);
    int update(T entity);
}
