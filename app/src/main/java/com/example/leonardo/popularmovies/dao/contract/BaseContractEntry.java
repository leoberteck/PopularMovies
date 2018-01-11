package com.example.leonardo.popularmovies.dao.contract;


import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.leonardo.popularmovies.dao.CursorWrapper;
import com.example.leonardo.popularmovies.entity.BaseEntity;

public interface BaseContractEntry<T extends BaseEntity> extends BaseColumns{

    T deserialize(CursorWrapper cursor, int position);

    ContentValues serialize(T entity);

    String[] getColumns();
    String getTableName();

    Uri getContentUri();
}
