package com.example.leonardo.popularmovies.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.leonardo.popularmovies.BR;
import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.api.MovieApi;
import com.example.leonardo.popularmovies.api.YoutubeApi;
import com.example.leonardo.popularmovies.mvp.MovieDetailMvp;
import com.example.leonardo.popularmovies.mvp.MovieDetailPresenter;
import com.example.leonardo.popularmovies.utils.ResourceUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends BaseAppCompatActivity implements MovieDetailMvp.MovieDetailActivityInterface {

    public static final String MOVIE_ID_EXTRA_KEY = "MOVIE_ID";
    private static final String YOUTUBE_APP_STORE_URL = "market://details?id=com.google.android.youtube";
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
        presenter = new MovieDetailPresenter(new MovieApi(), new YoutubeApi(), new ResourceUtils(this));
        binding.setVariable(BR.presenter, presenter);

        Intent intent = getIntent();
        if(intent.hasExtra(MOVIE_ID_EXTRA_KEY)){
            long movieId = intent.getLongExtra(MOVIE_ID_EXTRA_KEY, 0);
            presenter.setMovie(movieId, getCurrentLanguage());
            presenter.loadTrailers(movieId);
            presenter.loadReviews(movieId, getCurrentLanguage());
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

    @Override
    public void openTrailer(String videoUrl) {
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        if(youtubeIntent.resolveActivity(getPackageManager()) != null){
            startActivity(youtubeIntent);
        } else {
            Intent appStoreItent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_APP_STORE_URL));
            startActivity(appStoreItent);
        }
    }
}
