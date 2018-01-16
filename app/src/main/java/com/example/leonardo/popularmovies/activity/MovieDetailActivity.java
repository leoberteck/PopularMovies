package com.example.leonardo.popularmovies.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

    public static final String MOVIE_ID_EXTRA_KEY = "MOVIE_ID_EXTRA_KEY";
    public static final String MOVIE_DATABASE_ID_EXTRA_KEY = "MOVIE_DATABASE_ID_EXTRA_KEY";
    private static final String YOUTUBE_APP_STORE_URL = "market://details?id=com.google.android.youtube";
    private static final String TRAILER_RECYCLER_STATE_KEY = "TRAILER_RECYCLER_STATE_KEY";
    private static final String REVIEWS_RECYCLER_STATE_KEY = "REVIEWS_RECYCLER_STATE_KEY";
    private static final String SCROLL_POSITION_STATE_KEY = "NESTED_SCROLL_STATE_KEY";
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private MovieDetailMvp.MovieDetailPresenterInterface presenter;
    private ImageView imageViewMoviePoster;

    private RecyclerView movieTrailers;
    private RecyclerView movieReviews;
    private NestedScrollView nestedScrollView;

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
        presenter = new MovieDetailPresenter(new MovieApi(), new YoutubeApi(), new ResourceUtils(this), getContentResolver());
        binding.setVariable(BR.presenter, presenter);

        Intent intent = getIntent();
        long movieId = 0;
        long databaseId = 0;
        if(intent.hasExtra(MOVIE_ID_EXTRA_KEY)){
            movieId = intent.getLongExtra(MOVIE_ID_EXTRA_KEY, 0);
        }
        if(intent.hasExtra(MOVIE_DATABASE_ID_EXTRA_KEY)){
            databaseId = intent.getLongExtra(MOVIE_DATABASE_ID_EXTRA_KEY, 0);
        }
        presenter.setMovie(movieId, databaseId, getCurrentLanguage());
        presenter.loadTrailers(movieId);
        presenter.loadReviews(movieId, getCurrentLanguage());

        movieTrailers = findViewById(R.id.recyclerView_movieTrailers);
        movieReviews = findViewById(R.id.recyclerView_movieReviews);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        movieReviews.setHasFixedSize(false);
        movieReviews.addOnScrollListener(bottomScrollListener);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Parcelable trailersState = movieTrailers.getLayoutManager().onSaveInstanceState();
        state.putParcelable(TRAILER_RECYCLER_STATE_KEY, trailersState);

        Parcelable reviewsState = movieReviews.getLayoutManager().onSaveInstanceState();
        state.putParcelable(REVIEWS_RECYCLER_STATE_KEY, reviewsState);

        state.putIntArray(SCROLL_POSITION_STATE_KEY, new int[]{nestedScrollView.getScrollX(), nestedScrollView.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        movieTrailers.getLayoutManager().onRestoreInstanceState(savedInstanceState.getBundle(TRAILER_RECYCLER_STATE_KEY));
//        movieReviews.getLayoutManager().onRestoreInstanceState(savedInstanceState.getBundle(REVIEWS_RECYCLER_STATE_KEY));
//        final int[] scrollState = savedInstanceState.getIntArray(SCROLL_POSITION_STATE_KEY);
//        assert scrollState != null;
//        nestedScrollView.smoothScrollTo(scrollState[0], scrollState[1]);
        //This works but.... it is far from the best solution.
        //It looks like onRestoreInstanceState and onResume are running BEFORE data is
        //loaded into the layout. Maybe this is the nature of Databinding, or maybe it
        //has something to do with the layout includes (maybe the includes are done asynchronously)?
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                nestedScrollView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        nestedScrollView.smoothScrollTo(scrollState[0], scrollState[1]);
//                    }
//                });
//            }
//        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setMovieDetailActivity(this);
        //Even on onResume, the nested scrollview seems to not have any scrollable height....
        Log.i(TAG, String.valueOf("Scrollable Height : " + nestedScrollView.getChildAt(0).getHeight()));
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
            if(appStoreItent.resolveActivity(getPackageManager()) != null){
                startActivity(appStoreItent);
            }
        }
    }

    private final RecyclerView.OnScrollListener bottomScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!recyclerView.canScrollVertically(1)) {
                presenter.loadNextReviewsPage();
            }
        }
    };
}
