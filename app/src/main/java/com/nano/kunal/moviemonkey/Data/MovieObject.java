package com.nano.kunal.moviemonkey.Data;

import android.net.Uri;

/**
 * Created by Kunal on 2/12/2016.
 */
public class MovieObject {

    private static final String BASE_IMG_URL = "http://image.tmdb.org/t/p";
    public static final String LARGE_IMG_SIZE_PATH = "w780";
    private static final String IMG_SIZE_PATH = "w500";
    public static final String SMALL_IMG_SIZE_PATH = "w300";
    final String API_KEY_PARAM = "api_key";

    //Developer Api Key
    final String API_KEY = "85d62bbc194880880325544a0d180547";

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

    public String getBackdropUrl(){
        String url = Uri.parse(BASE_IMG_URL).buildUpon()
                .appendPath(LARGE_IMG_SIZE_PATH)
                .appendEncodedPath(backdrop_path)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build()
                .toString();

        return url;
    }

    public String getPosterUrl(String size){
        String url = Uri.parse(BASE_IMG_URL).buildUpon()
                .appendPath(size)
                .appendEncodedPath(poster_path)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build()
                .toString();

        return url;
    }

    public String getPosterUrl(){
       return getPosterUrl(IMG_SIZE_PATH);
    }

    public String getOverview() {
        return overview;
    }

    public String toString(){
        return original_title + " ( " + release_date + " ) - " + vote_average + " - " + overview;
    }
}
