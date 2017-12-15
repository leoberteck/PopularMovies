package com.example.leonardo.popularmovies.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.leonardo.popularmovies.BR;
import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.api.MovieApi;
import com.example.leonardo.popularmovies.mvp.MovieDetailMvp;
import com.example.leonardo.popularmovies.mvp.MovieDetailPresenter;
import com.example.leonardo.popularmovies.utils.ResourceUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends BaseAppCompatActivity implements MovieDetailMvp.MovieDetailActivityInterface {

    public static final String MOVIE_ID_EXTRA_KEY = "MOVIE_ID";
    private MovieDetailMvp.MovieDetailPresenterInterface presenter;
    private ImageView imageViewMoviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        imageViewMoviePoster = findViewById(R.id.image_view_movie_poster);
        presenter = new MovieDetailPresenter(new MovieApi(), new ResourceUtils(this));
        binding.setVariable(BR.presenter, presenter);

        Intent intent = getIntent();
        if(intent.hasExtra(MOVIE_ID_EXTRA_KEY)){
            long id = intent.getLongExtra(MOVIE_ID_EXTRA_KEY, 0);
            presenter.setMovie(id, getCurrentLanguage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setMovieDetailActivity(this);
    }

    @Override
    public void loadMoviePoster(String uri) {
        Picasso.with(this)
            .load(uri)
            .into(imageViewMoviePoster);
    }
}
