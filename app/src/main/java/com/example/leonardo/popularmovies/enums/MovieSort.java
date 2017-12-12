package com.example.leonardo.popularmovies.enums;


public enum MovieSort {
    TOP_RATED(1)
    , UPCOMING(2)
    , NOW_PLAYING(3);

    private int value;

    public int getValue() {
        return value;
    }

    MovieSort(int value) {
        this.value = value;
    }

    public static MovieSort valueOf(int value){
        MovieSort sort;
        switch (value){
            case 1:
            default:
                sort = TOP_RATED;
                break;
            case 2:
                sort = UPCOMING;
                break;
            case 3:
                sort = NOW_PLAYING;
                break;
        }
        return sort;
    }
}
