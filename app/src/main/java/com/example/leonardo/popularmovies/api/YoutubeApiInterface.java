package com.example.leonardo.popularmovies.api;

public interface YoutubeApiInterface {
    String getVideoThumbnailUrl(String movieId);
    String getVideoThumbnailUrl(String movieId, int thumbId);
    String getVideoWatchUrl(String videoKey);
}
