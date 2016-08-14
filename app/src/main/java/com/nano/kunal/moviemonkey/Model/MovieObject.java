package com.nano.kunal.moviemonkey.model;

import android.content.ContentValues;

import com.nano.kunal.moviemonkey.data.MovieContract;
import com.nano.kunal.moviemonkey.Utilities;

import java.util.Vector;

/**
 * Created by Kunal on 2/12/2016.
 */
public class MovieObject {

    private String original_title;
    private String release_date;
    private String overview;
    private String poster_path;
    private String backdrop_path;

    private double vote_average;
    private double popularity;

    private long movie_id;

    public MovieObject(String title, String release, String mOverview, String poster, String backdrop, double vote, double popular, long id){
        original_title = title;
        release_date = release;
        overview = mOverview;
        if(overview.equals(""))
            overview = "No summary available.";
        poster_path = poster;
        backdrop_path = backdrop;

        vote_average = vote;
        popularity = popular;

        movie_id = id;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getReleaseYear(){
        return release_date.substring(0, release_date.indexOf("-"));
    }

    public long getMovieId() {
        return movie_id;
    }

    public double getPopularity() {
        return popularity;
    }

    public double getVoteAverage() {
        return vote_average;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public String getBackdropUrl(){
        return Utilities.getBackdropUrl(backdrop_path);
    }

    public String getPosterUrl(String size){
        return Utilities.getPosterUrl(poster_path, size);
    }

    public String getPosterUrl(){
       return getPosterUrl(Utilities.IMG_SIZE_PATH);
    }

    public String getOverview() {
        return overview;
    }

    public String toString(){
        return original_title + " ( " + release_date + " ) - " + vote_average + " - " + overview;
    }

    public static ContentValues toContentValues (MovieObject movie){
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        values.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        values.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getVoteAverage());

        return values;
    }

    public static ContentValues[] toContentValuesArray(MovieObject[] movies) {
        Vector<ContentValues> vector = new Vector<>(movies.length);
        for (MovieObject movie : movies) {
            vector.add(toContentValues(movie));
        }
        return vector.toArray(new ContentValues[vector.size()]);
    }
}
