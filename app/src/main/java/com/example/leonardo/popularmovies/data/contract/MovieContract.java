package com.example.leonardo.popularmovies.data.contract;

import android.content.ContentValues;
import android.net.Uri;

import com.example.leonardo.popularmovies.data.CursorWrapper;
import com.example.leonardo.popularmovies.entity.Movie;

import java.util.Calendar;
import java.util.Date;

public class MovieContract {

    public static final String AUTHORITY = "com.example.leonardo.popularmovies";
    public static final String MOVIE_PATH = "movies";
    public static final Uri baseUri = Uri.parse("content://" + AUTHORITY);

    public static class MovieEntry implements BaseContractEntry<Movie> {

        public static final Uri contentUri = baseUri.buildUpon().appendPath(MOVIE_PATH).build();
        public static final String MOVIE_ID = "MOVIE_ID";
        public static final String TITLE = "TITLE";
        public static final String ORIGINAL_TITLE = "ORIGINAL_TITLE";
        public static final String POSTER_PATH = "POSTER_PATH";
        public static final String ORIGINAL_LANGUAGE = "ORIGINAL_LANGUAGE";
        public static final String OVERVIEW = "OVERVIEW";
        public static final String RELEASE_DATE = "RELEASE_DATE";
        public static final String RUNTIME = "RUNTIME";
        public static final String VOTE_AVERAGE = "VOTE_AVERAGE";

        private static final String[] columns = new String[]{
            _ID
            , MOVIE_ID
            , TITLE
            , ORIGINAL_TITLE
            , POSTER_PATH
            , ORIGINAL_LANGUAGE
            , OVERVIEW
            , RELEASE_DATE
            , RUNTIME
            , VOTE_AVERAGE
        };

        private static MovieEntry instance;

        public static MovieEntry getInstance() {
            if(instance == null){
                instance = new MovieEntry();
            }
            return instance;
        }

        @Override
        public Movie deserialize(CursorWrapper cursor, int position) {
            Movie entity = null;
            if(cursor.moveToPosition(position)){
                entity = new Movie();
                entity.setDatabaseId(cursor.getLong(_ID, 0L));
                entity.setMovieId(cursor.getLong(MOVIE_ID, 0L));
                entity.setTitle(cursor.getString(TITLE, null));
                entity.setOriginalTitle(cursor.getString(ORIGINAL_TITLE, null));
                entity.setPosterPath(cursor.getString(POSTER_PATH, null));
                entity.setOriginalLanguage(cursor.getString(ORIGINAL_LANGUAGE, null));
                entity.setOverview(cursor.getString(OVERVIEW, null));
                entity.setReleaseDate(new Date(cursor.getLong(RELEASE_DATE, Calendar.getInstance().getTime().getTime())));
                entity.setRuntime(cursor.getInt(RUNTIME, 0));
                entity.setVoteAverage(cursor.getDouble(VOTE_AVERAGE, 0.0));
            }
            return entity;
        }

        @Override
        public ContentValues serialize(Movie entity) {
            ContentValues values = new ContentValues();
            //values.put(_ID, entity.getDatabaseId());
            values.put(MOVIE_ID, entity.getMovieId());
            values.put(TITLE, entity.getTitle());
            values.put(ORIGINAL_TITLE, entity.getOriginalTitle());
            values.put(POSTER_PATH, entity.getPosterPath());
            values.put(ORIGINAL_LANGUAGE, entity.getOriginalLanguage());
            values.put(OVERVIEW, entity.getOverview());
            values.put(RELEASE_DATE, entity.getReleaseDate().getTime());
            values.put(RUNTIME, entity.getRuntime());
            values.put(VOTE_AVERAGE, entity.getVoteAverage());
            return values;
        }

        @Override
        public String[] getColumns() {
            return columns;
        }

        @Override
        public String getTableName() {
            return "MOVIE";
        }

        @Override
        public Uri getContentUri() {
            return contentUri;
        }

        public String getCreateTable(){
            return "CREATE TABLE " + getTableName() + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOVIE_ID + " INTEGER NOT NULL UNIQUE, "
                + TITLE + " TEXT, "
                + ORIGINAL_TITLE + " TEXT, "
                + POSTER_PATH + " TEXT, "
                + ORIGINAL_LANGUAGE + " TEXT, "
                + OVERVIEW + " TEXT, "
                + RELEASE_DATE + " INTEGER, "
                + RUNTIME + " INTEGER, "
                + VOTE_AVERAGE + " REAL);";
        }
    }
}
