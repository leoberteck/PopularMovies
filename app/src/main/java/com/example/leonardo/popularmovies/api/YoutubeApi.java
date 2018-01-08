package com.example.leonardo.popularmovies.api;


public class YoutubeApi implements YoutubeApiInterface {

    private static final String VIDEO_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/%s.jpg";
    private static final String VIDEO_WATCH_URL_WEB = "https://www.youtube.com/watch?v=%s";
    private static final String VIDEO_WATCH_URL_APP = "vnd.youtube:%s";

    @Override
    public String getVideoThumbnailUrl(String movieId) {
        return getVideoThumbnailUrl(movieId, 0);
    }

    @Override
    public String getVideoThumbnailUrl(String movieId, int thumbId) {
        return String.format(VIDEO_THUMBNAIL_URL, movieId, String.valueOf(thumbId));
    }

    @Override
    public String getVideoWatchUrl(String videoKey){
        return String.format(VIDEO_WATCH_URL_APP, videoKey);
    }
}
