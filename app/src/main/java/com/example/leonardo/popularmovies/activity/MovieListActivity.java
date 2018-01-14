package com.example.leonardo.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.example.leonardo.popularmovies.BR;
import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.adapters.MovieGridAdapter;
import com.example.leonardo.popularmovies.api.MovieApi;
import com.example.leonardo.popularmovies.enums.MovieSort;
import com.example.leonardo.popularmovies.mvp.MovieListMvp;
import com.example.leonardo.popularmovies.mvp.MovieListPresenter;
import com.example.leonardo.popularmovies.utils.Preferences;
import com.example.leonardo.popularmovies.utils.ResourceUtils;

public class MovieListActivity extends BaseAppCompatActivity implements MovieListMvp.MovieListActivityInterface {

    private static final String TAG = MovieListActivity.class.getSimpleName();
    private static MovieListMvp.MovieListPresenterInterface presenter;
    private Preferences preferences;
    private ResourceUtils resourceUtils;
    private String[] movieSortDescriptionArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourceUtils = new ResourceUtils(this);
        preferences = new Preferences(
            PreferenceManager.getDefaultSharedPreferences(this)
            , resourceUtils
        );
        AppCompatDelegate.setDefaultNightMode(preferences.getAppThemeDayNightMode());
        ViewDataBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list);
        if(presenter == null){
            presenter = new MovieListPresenter(new MovieApi(), getContentResolver());
        }
        viewDataBinding.setVariable(BR.presenter, presenter);

        RecyclerView movieGrid = findViewById(R.id.recyclerView_movies);
        movieGrid.setLayoutManager(new GridLayoutManager(this, getOptimalNumberOfColumns(this)));
        movieGrid.addOnScrollListener(bottomScrollListener);
        if(getAdapter() == null || getAdapter().getCurrentPage() == 0) {
            presenter.changeSort(preferences.getMovieSort(), getCurrentLanguage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setMovieListActivityInterface(this);
        MovieSort movieSort = preferences.getMovieSort();
        if(getAdapter().getMovieSort() != movieSort){
            presenter.changeSort(movieSort, getCurrentLocale(this).getLanguage());
        }
        setTitle(getTitle(movieSort));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(MovieListActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClick(long movieId, long databaseId) {
        Intent intent = new Intent(MovieListActivity.this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_ID_EXTRA_KEY, movieId);
        intent.putExtra(MovieDetailActivity.MOVIE_DATABASE_ID_EXTRA_KEY, databaseId);
        startActivity(intent);
    }

    private int getOptimalNumberOfColumns(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return Math.round(dpWidth / 185);
    }

    private final RecyclerView.OnScrollListener bottomScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!recyclerView.canScrollVertically(1)) {
                presenter.loadNextPage();
            }
        }
    };

    private MovieGridAdapter getAdapter(){
        return presenter.getAdapter();
    }

    private String getTitle(MovieSort movieSort){
        return getMovieSortDescriptionArray()[movieSort.getValue()-1];
    }

    private String[] getMovieSortDescriptionArray() {
        if(movieSortDescriptionArray == null){
            movieSortDescriptionArray = resourceUtils.getStringArray(R.array.movie_sort_values_descripton);
        }
        return movieSortDescriptionArray;
    }
}
