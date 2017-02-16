package com.thayane.popularmovies;

/**
 * Created by Thayane on 13/02/2017.
 */

public class MovieData {

    String id, title, originalTitle, imagePath, releaseDate, overview, voteAverage;

    public MovieData(String id, String overview, String releaseDate, String imagePath, String originalTitle, String title, String voteAverage) {
        this.voteAverage = voteAverage;
        this.id = id;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.imagePath = imagePath;
        this.originalTitle = originalTitle;
        this.title = title;
    }

    public MovieData(String imagePath, String id) {
        this.imagePath = imagePath;
        this.id = id;
    }
}
