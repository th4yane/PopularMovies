package com.thayane.popularmovies;

/**
 * Created by Thayane on 13/02/2017.
 */

public class MovieData {

    String id, title, imagePath, releaseDate, overview, voteAverage, runtime;
    String[] video;
    ReviewData[] reviews;

    public MovieData(String id, String overview, String releaseDate, String imagePath,
                     String title, String voteAverage, String[] video, String runtime, ReviewData[] reviews) {
        this.voteAverage = voteAverage;
        this.id = id;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.imagePath = imagePath;
        this.title = title;
        this.video = video;
        this.runtime = runtime;
        this.reviews = reviews;
    }

    public MovieData(String imagePath, String id) {
        this.imagePath = imagePath;
        this.id = id;
    }

    public MovieData(String id, String title, String imagePath){
        this.id =id;
        this.title = title;
        this.imagePath = imagePath;
    }
}
