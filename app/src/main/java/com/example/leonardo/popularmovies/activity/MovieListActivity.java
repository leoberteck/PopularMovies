package com.example.leonardo.popularmovies.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.leonardo.popularmovies.BR;
import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.api.MovieApi;
import com.example.leonardo.popularmovies.enums.MovieSort;
import com.example.leonardo.popularmovies.mvp.MovieListMvp;
import com.example.leonardo.popularmovies.mvp.MovieListPresenter;

public class MovieListActivity extends AppCompatActivity implements MovieListMvp.MovieListActivityInterface {

    private static MovieListMvp.MovieListPresenterInterface presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list);
        setContentView(R.layout.activity_movie_list);
        if(presenter == null){
            presenter = new MovieListPresenter(new MovieApi());
        }
        viewDataBinding.setVariable(BR.presenter, presenter);
        if(presenter.getAdapter() == null || presenter.getAdapter().getCurrentPage() == 0){
            presenter.changeSort(MovieSort.NOW_PLAYING, "pt_BR");
        }
    }
}
