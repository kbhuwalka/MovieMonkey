package com.nano.kunal.moviemonkey;

import android.net.Uri;

import com.nano.kunal.moviemonkey.Data.MovieContract;

/**
 * Created by Kunal on 6/27/2016.
 */
public class Utilities {
    //Developer Api Key
    public static final String API_KEY = "85d62bbc194880880325544a0d180547";
    public static final int MEDIA_TYPE_POSTER =10;
    public static final int MEDIA_TYPE_BACKDROP =20;
    public static final int MEDIA_TYPE_VIDEO =30;
    private static final String BASE_IMG_URL = "http://image.tmdb.org/t/p";
    private static final String API_KEY_PARAM = "api_key";

    public static final String IMG_SIZE_PATH = "w500";
    public static final String SMALL_IMG_SIZE_PATH = "w300";
    public static final String LARGE_IMG_SIZE_PATH = "w780";


    public static String getBackdropUrl(String path){
        String url = Uri.parse(BASE_IMG_URL).buildUpon()
                .appendPath(LARGE_IMG_SIZE_PATH)
                .appendEncodedPath(path)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build()
                .toString();

        return url;
    }

    public static String getPosterUrl(String path, String size){
        String url = Uri.parse(BASE_IMG_URL).buildUpon()
                .appendPath(size)
                .appendEncodedPath(path)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build()
                .toString();

        return url;
    }

    public static final class DetailsProjection{

        public static final String[] DETAILS_PROJECTION={
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.MovieEntry.COLUMN_RATING,
                MovieContract.MovieEntry.COLUMN_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_BACKDROP_PATH
        };
        public static final int COL_ID = 0;
        public static final int COL_MOVIE_ID = 1;
        public static final int COL_TITLE = 2;
        public static final int COL_RELEASE_DATE = 3;
        public static final int COL_RATING = 4;
        public static final int COL_OVERVIEW = 5;
        public static final int COL_POSTER_PATH = 6;
        public static final int COL_BACKDROP_PATH = 7;
    }

    public static final class MediaProjection{
        public static final String[] MEDIA_PROJECTION = {
                MovieContract.MediaEntry._ID,
                MovieContract.MediaEntry.COLUMN_PATH,
                MovieContract.MediaEntry.COLUMN_MEDIA_TYPE
        };

        public static final int COL_ID = 0;
        public static final int COL_PATH = 1;
        public static final int COL_TYPE = 2;
    }

    public static final class ReviewProjection{
        public static final String[] REVIEWS_PROJECTION = {
                MovieContract.ReviewsEntry._ID,
                MovieContract.ReviewsEntry.COLUMN_AUTHOR,
                MovieContract.ReviewsEntry.COLUMN_CONTENT
        };

        public static final int COL_ID = 0;
        public static final int COL_AUTHOR = 1;
        public static final int COL_CONTENT = 2;
    }

}
