package com.example.leonardo.popularmovies.mvp;

import android.content.ContentResolver;
import android.databinding.BaseObservable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.leonardo.popularmovies.App;
import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.adapters.MovieReviewAdapter;
import com.example.leonardo.popularmovies.adapters.MovieTrailerAdapter;
import com.example.leonardo.popularmovies.api.MovieAPIInterface;
import com.example.leonardo.popularmovies.api.YoutubeApiInterface;
import com.example.leonardo.popularmovies.data.dao.MovieDao;
import com.example.leonardo.popularmovies.entity.Movie;
import com.example.leonardo.popularmovies.entity.ReviewPaginatedResult;
import com.example.leonardo.popularmovies.entity.Trailer;
import com.example.leonardo.popularmovies.entity.TrailerResult;
import com.example.leonardo.popularmovies.enums.ImageSize;
import com.example.leonardo.popularmovies.utils.ResourceUtils;
import com.example.leonardo.popularmovies.BR;
import java.text.DateFormat;

import static com.example.leonardo.popularmovies.data.dao.AbstractDaoInterface.OnTaskFinishListener;

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
    @NonNull
    private final ContentResolver contentResolver;
    private Movie movie;
    private MovieDetailMvp.MovieDetailActivityInterface movieDetailActivity;
    private MovieTrailerAdapter movieTrailerAdapter;
    private MovieReviewAdapter movieReviewAdapter;
    private MovieDao movieDao;
    private final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

    private int favoriteIcon = R.drawable.ic_favorite_border_tinted_32dp;

    public MovieDetailPresenter(
        @NonNull MovieAPIInterface movieAPIInterface
        , @NonNull YoutubeApiInterface youtubeApiInterface
        , @NonNull ResourceUtils resourceUtils
        , @NonNull ContentResolver contentResolver)
    {
        this.movieAPIInterface = movieAPIInterface;
        this.youtubeApiInterface = youtubeApiInterface;
        this.resourceUtils = resourceUtils;
        this.contentResolver = contentResolver;
        movieDao = new MovieDao(contentResolver);
    }

    //region binding properties
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
    public int getFavoriteIcon() {
        return favoriteIcon;
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

    //endregion

    //region public methods
    @Override
    public void setMovie(final long movieId, final long databaseId, final String locale) {
        //If network connection is available tries to find the movie by the APIW
        if(App.getInstance().isNetworkAvailable()){
            new FindMovieTaskOnAPI(movieAPIInterface, new OnTaskFinishListener<Movie>() {
                @Override
                public void onTaskFinish(Exception ex, Movie result) {
                    if(ex == null){
                        changeMovie(result);
                        if(MovieDetailPresenter.this.movie != null){
                            setDatabaseId(databaseId);
                        }
                        //Verifies if the movie is in the database;
                        movieDao.findByMovieId(movieId, new OnTaskFinishListener<Movie>() {
                            @Override
                            public void onTaskFinish(Exception ex, Movie result) {
                                //If the movie is in the database, update the favorite icon accordingly
                                setDatabaseId(result != null ? result.getDatabaseId() : 0);
                            }
                        });
                    } else {
                        movieDetailActivity.showErrorMessage(R.string.error, R.string.api_error_message, ex);
                    }
                }
            }).run(movieId, locale);
        }else{
            //If there is no internet connection, tries to find the movie in the database
            movieDao.findOneById(databaseId, new OnTaskFinishListener<Movie>() {
                @Override
                public void onTaskFinish(Exception ex, Movie result) {
                    changeMovie(result);
                    if(MovieDetailPresenter.this.movie != null){
                        setDatabaseId(databaseId);
                    }
                }
            });
        }
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
        new FindReviewsTask(movieAPIInterface, new OnTaskFinishListener<ReviewPaginatedResult>() {
            @Override
            public void onTaskFinish(Exception ex, ReviewPaginatedResult result) {
                if(ex == null){
                    movieReviewAdapter.addPage(result);
                    notifyPropertyChanged(BR.reviewsVisibility);
                    notifyPropertyChanged(BR.reviewsErrorVisibility);
                } else {
                    movieDetailActivity.showErrorMessage(R.string.error, R.string.api_error_message, ex);
                }
            }
        }).run(id, movieReviewAdapter.getCurrentPage() + 1, locale);
    }

    @Override
    public void loadNextReviewsPage() {
        if(movieReviewAdapter.getCurrentPage() < movieReviewAdapter.getTotalPages() || movieReviewAdapter.getCurrentPage() == 0){
            loadReviews(movie.getMovieId(), movieReviewAdapter.getLocale());
        }
    }

    @Override
    public void loadTrailers(long id) {
        new FindVideosTask(movieAPIInterface, new OnTaskFinishListener<TrailerResult>() {
            @Override
            public void onTaskFinish(Exception ex, TrailerResult result) {
                if(ex == null){
                    if(result != null && result.getResults() != null && result.getResults().size() > 0){
                        movieTrailerAdapter = new MovieTrailerAdapter(
                            result.getResults()
                            , youtubeApiInterface
                            , MovieDetailPresenter.this
                        );
                        notifyPropertyChanged(BR.trailerAdapter);
                        notifyPropertyChanged(BR.trailersVisibility);
                        notifyPropertyChanged(BR.trailersErrorVisibility);
                    }
                } else {
                    movieDetailActivity.showErrorMessage(R.string.error, R.string.api_error_message, ex);
                }
            }
        }).run(id);
    }

    @Override
    public void onFavoriteClick() {
        if(movie != null){
            if(movie.getDatabaseId() != 0){
                movieDao.delete(movie.getDatabaseId(), null);
                setDatabaseId(0);
            } else {
                movieDao.insert(movie, new OnTaskFinishListener<Movie>() {
                    @Override
                    public void onTaskFinish(Exception ex, Movie result) {
                        MovieDetailPresenter.this.movie = result;
                        setDatabaseId(movie.getDatabaseId());
                    }
                });
            }
        }
    }


    @Override
    public void onItemClick(Trailer trailer) {
        movieDetailActivity.openTrailer(
            youtubeApiInterface.getVideoWatchUrl(
                trailer.getKey()
            )
        );
    }

    //endregion

    //region private methods

    private void setDatabaseId(long databaseId){
        movie.setDatabaseId(databaseId);
        if(databaseId != 0){
            setFavoriteIcon(R.drawable.ic_favorite_tinted_32dp);
        } else {
            setFavoriteIcon(R.drawable.ic_favorite_border_tinted_32dp);
        }
    }

    private void changeMovie(Movie movie){
        this.movie = movie;
        notifyPropertyChanged(BR._all);
        if(movie != null){
            if(movieDetailActivity != null){
                movieDetailActivity.loadMoviePoster(movieAPIInterface.getImageUrl(movie.getPosterPath(), ImageSize.W500));
            }
        }
    }

    private void setFavoriteIcon(int favoriteIcon) {
        this.favoriteIcon = favoriteIcon;
        notifyPropertyChanged(BR.favoriteIcon);
    }

    //endregion

    //region API BACKGROUND TASKS
    private static class FindMovieTaskOnAPI extends AsyncTask<Object, Void, Movie>{

        @NonNull
        private final OnTaskFinishListener<Movie> listener;
        @NonNull
        private final MovieAPIInterface movieAPIInterface;
        private Exception e;

        FindMovieTaskOnAPI(@NonNull MovieAPIInterface movieAPIInterface, @NonNull OnTaskFinishListener<Movie> listener) {
            this.listener = listener;
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
            listener.onTaskFinish(e, movie);
        }

        void run(long id, String locale){
            execute(id, locale);
        }

    }

    private static class FindVideosTask extends AsyncTask<Object, Void, TrailerResult> {

        @NonNull
        private final OnTaskFinishListener<TrailerResult> listener;
        @NonNull
        private final MovieAPIInterface movieAPIInterface;
        private Exception e;

        FindVideosTask(@NonNull MovieAPIInterface movieAPIInterface, @NonNull OnTaskFinishListener<TrailerResult> listener) {
            this.movieAPIInterface = movieAPIInterface;
            this.listener = listener;
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
            listener.onTaskFinish(e, trailerResult);
        }

        void run (long id){
            this.execute(id);
        }
    }

    private static class FindReviewsTask extends AsyncTask<Object, Void, ReviewPaginatedResult> {

        @NonNull
        private OnTaskFinishListener<ReviewPaginatedResult> listener;
        @NonNull
        private final MovieAPIInterface movieAPIInterface;
        private Exception e;

        private FindReviewsTask(@NonNull MovieAPIInterface movieAPIInterface, @NonNull OnTaskFinishListener<ReviewPaginatedResult> listener) {
            this.movieAPIInterface = movieAPIInterface;
            this.listener = listener;
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
            listener.onTaskFinish(e, reviewPaginatedResult);
        }

        void run(long id, int page, String locale){
            this.execute(id, page, locale);
        }
    }
    //endregion
}