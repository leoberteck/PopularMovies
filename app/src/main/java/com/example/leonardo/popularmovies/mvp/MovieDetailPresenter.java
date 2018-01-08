package com.example.leonardo.popularmovies.mvp;

import android.databinding.BaseObservable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.leonardo.popularmovies.BR;
import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.adapters.MovieReviewAdapter;
import com.example.leonardo.popularmovies.adapters.MovieTrailerAdapter;
import com.example.leonardo.popularmovies.api.MovieAPIInterface;
import com.example.leonardo.popularmovies.api.YoutubeApiInterface;
import com.example.leonardo.popularmovies.entity.Movie;
import com.example.leonardo.popularmovies.entity.ReviewPaginatedResult;
import com.example.leonardo.popularmovies.entity.Trailer;
import com.example.leonardo.popularmovies.entity.TrailerResult;
import com.example.leonardo.popularmovies.enums.ImageSize;
import com.example.leonardo.popularmovies.utils.ResourceUtils;

import java.text.DateFormat;

public class MovieDetailPresenter
    extends BaseObservable
    implements MovieDetailMvp.MovieDetailPresenterInterface
        , MovieTrailerAdapter.ItemClickListener {

    @NonNull
    private final MovieAPIInterface movieAPIInterface;
    @NonNull
    private final YoutubeApiInterface youtubeApiInterface;
    @NonNull
    private final ResourceUtils resourceUtils;
    private Movie movie;
    private MovieDetailMvp.MovieDetailActivityInterface movieDetailActivity;
    private MovieTrailerAdapter movieTrailerAdapter;
    private MovieReviewAdapter movieReviewAdapter;

    private final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

    public MovieDetailPresenter(
            @NonNull MovieAPIInterface movieAPIInterface
            , @NonNull YoutubeApiInterface youtubeApiInterface
            , @NonNull ResourceUtils resourceUtils)
    {
        this.movieAPIInterface = movieAPIInterface;
        this.youtubeApiInterface = youtubeApiInterface;
        this.resourceUtils = resourceUtils;
    }

    @Override
    public String getTitle() {
        return movie != null ? movie.getTitle() : null;
    }

    @Override
    public String getOriginalTitle() {
        return movie != null ? movie.getOriginalTitle() : null;
    }

    @Override
    public String getReleaseDate() {
        return movie != null && movie.getReleaseDate() != null ? dateFormat.format(movie.getReleaseDate()) : null;
    }

    @Override
    public String getDuration() {
        return movie != null ? String.valueOf(movie.getRuntime()) + resourceUtils.getString(R.string.minutes_abreviated) : null;
    }

    @Override
    public String getScore() {
        return movie != null ? String.valueOf(movie.getVoteAverage()) + "/10" : null;
    }

    @Override
    public String getOverview() {
        return movie != null ? movie.getOverview() : null;
    }

    @Override
    public void setMovie(long movieId, String locale) {
        new FindMovieTask(this, movieAPIInterface).run(movieId, locale);
    }

    @Override
    public void setMovieDetailActivity(MovieDetailMvp.MovieDetailActivityInterface movieDetailActivity) {
        this.movieDetailActivity = movieDetailActivity;
    }

    @Override
    public void loadReviews(long id, String locale) {
        if(movieReviewAdapter == null){
            movieReviewAdapter = new MovieReviewAdapter(null, locale);
            notifyPropertyChanged(BR.reviewsAdapter);
            notifyPropertyChanged(BR.reviewsVisibility);
            notifyPropertyChanged(BR.reviewsErrorVisibility);
        }
        new FindReviewsTask(this, movieAPIInterface).run(id, movieReviewAdapter.getCurrentPage() + 1, locale);
    }

    @Override
    public void loadTrailers(long id) {
        new FindVideosTask(this, movieAPIInterface).run(id);
    }

    private void onMovieLoadFinish(@Nullable Movie movie){
        MovieDetailPresenter.this.movie = movie;
        notifyPropertyChanged(BR._all);
        if(movieDetailActivity != null && movie != null){
            movieDetailActivity.loadMoviePoster(movieAPIInterface.getImageUrl(movie.getPosterPath(), ImageSize.W500));
        }
    }

    private void onTrailerLoadFinish(@Nullable TrailerResult trailerResult){
        if(trailerResult != null && trailerResult.getResults() != null && trailerResult.getResults().size() > 0){
            movieTrailerAdapter = new MovieTrailerAdapter(
                trailerResult.getResults()
                , youtubeApiInterface
                , this
            );
            notifyPropertyChanged(BR.trailerAdapter);
            notifyPropertyChanged(BR.trailersVisibility);
            notifyPropertyChanged(BR.trailersErrorVisibility);
        }
    }

    private void onReviewLoadFinish(@Nullable ReviewPaginatedResult reviewPaginatedResult) {
        movieReviewAdapter.addPage(reviewPaginatedResult);
        notifyPropertyChanged(BR.reviewsVisibility);
        notifyPropertyChanged(BR.reviewsErrorVisibility);
    }

    @Override
    public RecyclerView.Adapter getTrailerAdapter() {
        return movieTrailerAdapter;
    }

    @Override
    public int getTrailersVisibility() {
        return movieTrailerAdapter != null && movieTrailerAdapter.getItemCount() > 0 ? View.VISIBLE : View.GONE;
    }

    @Override
    public int getTrailersErrorVisibility() {
        return getTrailersVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
    }

    @Override
    public RecyclerView.Adapter getReviewsAdapter() {
        return movieReviewAdapter;
    }

    @Override
    public int getReviewsVisibility() {
        return movieReviewAdapter != null && movieReviewAdapter.getItemCount() > 0 ? View.VISIBLE : View.GONE;
    }

    @Override
    public int getReviewsErrorVisibility() {
        return getReviewsVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
    }

    @Override
    public void onItemClick(Trailer trailer) {
        movieDetailActivity.openTrailer(
            youtubeApiInterface.getVideoWatchUrl(
                trailer.getKey()
            )
        );
    }

    private static class FindMovieTask extends AsyncTask<Object, Void, Movie>{

        @NonNull
        private final MovieDetailPresenter caller;
        @NonNull
        private final MovieAPIInterface movieAPIInterface;
        private Exception e;

        FindMovieTask(@NonNull MovieDetailPresenter caller, @NonNull MovieAPIInterface movieAPIInterface) {
            this.caller = caller;
            this.movieAPIInterface = movieAPIInterface;
        }

        @Override
        protected Movie doInBackground(Object... objects) {
            long id = (long) objects[0];
            String locale = (String) objects[1];
            Movie result = null;
            try {
                result = movieAPIInterface.getDetails(id, locale);
            }catch (Exception e){
                this.e = e;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            if(e != null){
                caller.movieDetailActivity.showErrorMessage(R.string.error, R.string.api_error_message, e);
            }else{
                caller.onMovieLoadFinish(movie);
            }
        }

        void run(long id, String locale){
            execute(id, locale);
        }
    }

    private static class FindVideosTask extends AsyncTask<Object, Void, TrailerResult> {

        @NonNull
        private final MovieDetailPresenter caller;
        @NonNull
        private final MovieAPIInterface movieAPIInterface;
        private Exception e;

        FindVideosTask(@NonNull MovieDetailPresenter caller, @NonNull MovieAPIInterface movieAPIInterface) {
            this.caller = caller;
            this.movieAPIInterface = movieAPIInterface;
        }

        @Override
        protected TrailerResult doInBackground(Object... params) {
            long id = (long) params[0];
            TrailerResult trailerResult = null;
            try {
                trailerResult = movieAPIInterface.getTrailers(id);
            } catch (Exception e){
                this.e = e;
            }
            return trailerResult;
        }

        @Override
        protected void onPostExecute(TrailerResult trailerResult) {
            super.onPostExecute(trailerResult);
            if(e != null){
                caller.movieDetailActivity.showErrorMessage(R.string.error, R.string.api_error_message, e);
            } else {
                caller.onTrailerLoadFinish(trailerResult);
            }
        }

        void run (long id){
            this.execute(id);
        }
    }

    private static class FindReviewsTask extends AsyncTask<Object, Void, ReviewPaginatedResult> {

        @NonNull
        private final MovieDetailPresenter caller;
        @NonNull
        private final MovieAPIInterface movieAPIInterface;
        private Exception e;

        private FindReviewsTask(@NonNull MovieDetailPresenter caller, @NonNull MovieAPIInterface movieAPIInterface) {
            this.caller = caller;
            this.movieAPIInterface = movieAPIInterface;
        }

        @Override
        protected ReviewPaginatedResult doInBackground(Object... params) {
            long id = (long) params[0];
            int page = (int) params[1];
            String locale = (String) params[2];
            ReviewPaginatedResult reviewPaginatedResult = null;
            try {
                reviewPaginatedResult = movieAPIInterface.getReviews(id, page, locale);
            } catch (Exception e){
                this.e = e;
            }
            return reviewPaginatedResult;
        }

        @Override
        protected void onPostExecute(ReviewPaginatedResult reviewPaginatedResult) {
            super.onPostExecute(reviewPaginatedResult);
            if(e != null){
                caller.movieDetailActivity.showErrorMessage(R.string.error, R.string.api_error_message, e);
            } else {
                caller.onReviewLoadFinish(reviewPaginatedResult);
            }
        }

        void run(long id, int page, String locale){
            this.execute(id, page, locale);
        }
    }
}
