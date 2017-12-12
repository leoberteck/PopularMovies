package com.example.leonardo.popularmovies.mvp;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.example.leonardo.popularmovies.BR;
import com.example.leonardo.popularmovies.adapters.MovieGridAdapter;
import com.example.leonardo.popularmovies.api.MovieAPIInterface;
import com.example.leonardo.popularmovies.entity.MoviePaginatedResult;
import com.example.leonardo.popularmovies.enums.MovieSort;

@BindingMethods(value = {
    @BindingMethod(type = RecyclerView.class, attribute = "android:adapter", method = "setAdapter")
})
public class MovieListPresenter extends BaseObservable implements MovieListMvp.MovieListPresenterInterface {

    private MovieGridAdapter adapter;
    private MovieAPIInterface movieApi;

    public MovieListPresenter(MovieAPIInterface movieApi) {
        this.movieApi = movieApi;
    }

    @Bindable
    @Override
    public MovieGridAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MovieGridAdapter adapter) {
        this.adapter = adapter;
        notifyPropertyChanged(BR._all);
    }

    @Override
    public void changeSort(MovieSort movieSort, String locale){
        setAdapter(new MovieGridAdapter(movieApi, movieSort, locale));
        loadNextPage();
    }

    @Override
    public void loadNextPage(){
        new MovieQueryTask(this, movieApi).run(
            getAdapter().getMovieSort()
            , getAdapter().getCurrentPage() + 1
            , getAdapter().getLocale()
        );
    }

    private static class MovieQueryTask extends AsyncTask<Object, Void, MoviePaginatedResult> {
        private MovieListPresenter caller;
        MovieAPIInterface movieApi;

        MovieQueryTask(MovieListPresenter caller, MovieAPIInterface movieApi) {
            this.caller = caller;
            this.movieApi = movieApi;
        }

        @Override
        protected MoviePaginatedResult doInBackground(Object... objects) {
            MovieSort movieSort = (MovieSort) objects[0];
            int page = (int) objects[1];
            String locale = (String) objects[2];
            MoviePaginatedResult result = null;
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
            }
            return result;
        }

        @Override
        protected void onPostExecute(MoviePaginatedResult movies) {
            super.onPostExecute(movies);
            caller.getAdapter().addPage(movies);
        }

        protected void run(MovieSort movieSort, int page, String locale){
            execute(movieSort, page, locale);
        }
    }
}
