package com.example.leonardo.popularmovies.enums;

public enum ImageSize {
    W92("w92")
    , W154("w154")
    , W185("w185")
    , W342("w342")
    , W500("w500")
    , W780("w780")
    , ORIGINAL("original");

    private String value;

    public String getValue() {
        return value;
    }

    ImageSize(String value) {
        this.value = value;
    }
}
