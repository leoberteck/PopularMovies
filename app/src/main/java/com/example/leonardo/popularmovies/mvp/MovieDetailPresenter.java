package com.example.leonardo.popularmovies.mvp;

import android.databinding.BaseObservable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.leonardo.popularmovies.BR;
import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.api.MovieAPIInterface;
import com.example.leonardo.popularmovies.entity.Movie;
import com.example.leonardo.popularmovies.enums.ImageSize;
import com.example.leonardo.popularmovies.utils.ResourceUtils;

import java.text.DateFormat;

public class MovieDetailPresenter extends BaseObservable implements MovieDetailMvp.MovieDetailPresenterInterface {

    private Movie movie;
    @NonNull
    private MovieAPIInterface movieAPIInterface;
    @NonNull
    private ResourceUtils resourceUtils;
    private MovieDetailMvp.MovieDetailActivityInterface movieDetailActivity;

    private final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

    public MovieDetailPresenter(@NonNull MovieAPIInterface movieAPIInterface, @NonNull ResourceUtils resourceUtils) {
        this.movieAPIInterface = movieAPIInterface;
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
        new FindMovieTask(new FindMovieTask.OnTaskFinishListener() {
            @Override
            public void onTaskFinish(@Nullable Movie movie) {
                MovieDetailPresenter.this.movie = movie;
                notifyPropertyChanged(BR._all);
                if(movieDetailActivity != null){
                    movieDetailActivity.loadMoviePoster(movieAPIInterface.getImageUrl(movie.getPosterPath(), ImageSize.W500));
                }
            }
        }, movieAPIInterface).run(movieId, locale);
    }

    @Override
    public void setMovieDetailActivity(MovieDetailMvp.MovieDetailActivityInterface movieDetailActivity) {
        this.movieDetailActivity = movieDetailActivity;
    }

    private static class FindMovieTask extends AsyncTask<Object, Void, Movie>{

        @NonNull
        private OnTaskFinishListener listener;
        @NonNull
        private MovieAPIInterface movieAPIInterface;

        FindMovieTask(@NonNull OnTaskFinishListener listener, @NonNull MovieAPIInterface movieAPIInterface) {
            this.listener = listener;
            this.movieAPIInterface = movieAPIInterface;
        }

        @Override
        protected Movie doInBackground(Object... objects) {
            long id = (long) objects[0];
            String locale = (String) objects[1];
            return movieAPIInterface.getDetails(id, locale);
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            listener.onTaskFinish(movie);
        }

        void run(long id, String locale){
            execute(id, locale);
        }

        interface OnTaskFinishListener{
            void onTaskFinish(@Nullable Movie movie);
        }
    }
}
