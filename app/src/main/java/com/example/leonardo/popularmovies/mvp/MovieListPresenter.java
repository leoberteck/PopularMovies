package com.example.leonardo.popularmovies.mvp;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.adapters.MovieGridAdapter;
import com.example.leonardo.popularmovies.api.MovieAPIInterface;
import com.example.leonardo.popularmovies.data.contract.MovieContract;
import com.example.leonardo.popularmovies.data.dao.AbstractDaoInterface;
import com.example.leonardo.popularmovies.data.dao.MovieDao;
import com.example.leonardo.popularmovies.entity.Movie;
import com.example.leonardo.popularmovies.entity.MoviePaginatedResult;
import com.example.leonardo.popularmovies.enums.MovieSort;
import com.example.leonardo.popularmovies.BR;
import java.util.List;

import static com.example.leonardo.popularmovies.mvp.MovieListMvp.MovieListActivityInterface;
import static com.example.leonardo.popularmovies.mvp.MovieListMvp.MovieListPresenterInterface;


public class MovieListPresenter extends BaseObservable implements MovieListPresenterInterface, MovieGridAdapter.ItemClickListener {

    private MovieGridAdapter adapter;
    @NonNull
    private final MovieAPIInterface movieApi;
    @NonNull
    private final ContentResolver contentResolver;
    private final MovieDao movieDao;
    private MovieListActivityInterface movieListActivityInterface;

    public MovieListPresenter(@NonNull MovieAPIInterface movieApi, @NonNull ContentResolver contentResolver) {
        this.movieApi = movieApi;
        this.contentResolver = contentResolver;
        movieDao = new MovieDao(contentResolver);

        //Register a ContentObserver for the Movie Content Uri, so that the view knows
        // when the user unfavorite a movie and then remove it from the list.
        this.contentResolver.registerContentObserver(
            MovieContract.MovieEntry.getInstance().getContentUri()
            , false
            , new ContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                    if(getAdapter().getMovieSort() == MovieSort.FAVORITES){
                        changeSort(getAdapter().getMovieSort(), getAdapter().getLocale());
                    }
                }
            }
        );
    }

    @Bindable
    @Override
    public MovieGridAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MovieGridAdapter adapter) {
        this.adapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Override
    public void changeSort(MovieSort movieSort, String locale){
        setAdapter(new MovieGridAdapter(movieApi, movieSort, locale, this));
        loadNextPage();
    }

    @Override
    public void loadNextPage(){
        if(getAdapter().getCurrentPage() < getAdapter().getTotalPages() || getAdapter().getCurrentPage() == 0){
            if(getAdapter().getMovieSort() == MovieSort.FAVORITES){
                movieDao.query(new AbstractDaoInterface.OnTaskFinishListener<List<Movie>>() {
                    @Override
                    public void onTaskFinish(Exception ex, List<Movie> result) {
                        MoviePaginatedResult onePageResult = new MoviePaginatedResult();
                        onePageResult.setPage(1);
                        onePageResult.setResults(result);
                        onePageResult.setTotalPages(1);
                        onePageResult.setTotalResults(result != null ? result.size() : 0);
                        getAdapter().addPage(onePageResult);
                    }
                });
            }else{
                new MovieQueryTask(this, movieApi).run(
                        getAdapter().getMovieSort()
                        , getAdapter().getCurrentPage() + 1
                        , getAdapter().getLocale()
                );
            }
        }
    }

    @Override
    public void onItemClick(Movie movie) {
        movieListActivityInterface.onMovieClick(movie.getMovieId(), movie.getDatabaseId());
    }

    @Override
    public void setMovieListActivityInterface(MovieListActivityInterface movieListActivityInterface) {
        this.movieListActivityInterface = movieListActivityInterface;
    }

    private static class MovieQueryTask extends AsyncTask<Object, Void, MoviePaginatedResult> {
        private static final String TAG = MovieQueryTask.class.getSimpleName();
        @NonNull
        private final MovieListPresenter caller;
        @NonNull
        private final MovieAPIInterface movieApi;

        private Exception e;

        MovieQueryTask(@NonNull MovieListPresenter caller, @NonNull MovieAPIInterface movieApi) {
            this.caller = caller;
            this.movieApi = movieApi;
        }

        @Override
        protected MoviePaginatedResult doInBackground(Object... objects) {
            MovieSort movieSort = (MovieSort) objects[0];
            int page = (int) objects[1];
            String locale = (String) objects[2];
            MoviePaginatedResult result = null;
            try{
                switch (movieSort){
                    case UPCOMING:
                        result = movieApi.listUpcoming(page, locale);
                        break;
                    case TOP_RATED:
                        result = movieApi.listTopRated(page, locale);
                        break;
                    case NOW_PLAYING:
                        result = movieApi.listNowPlayng(page, locale);
                        break;
                    case POPULAR:
                        result = movieApi.listPopular(page, locale);
                        break;
                }
            }catch (Exception e){
                this.e = e;
            }
            return result;
        }

        @Override
        protected void onPostExecute(MoviePaginatedResult movies) {
            super.onPostExecute(movies);
            if(e != null){
                caller.movieListActivityInterface.showErrorMessage(R.string.error, R.string.api_error_message, e);
            }else{
                caller.getAdapter().addPage(movies);
            }
        }

        void run(MovieSort movieSort, int page, String locale){
            execute(movieSort, page, locale);
            Log.i(TAG, "Loading page : " + page + " from " + movieSort.toString());
        }
    }
}
