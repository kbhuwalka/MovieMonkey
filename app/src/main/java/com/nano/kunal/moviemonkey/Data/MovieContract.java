package com.nano.kunal.moviemonkey.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Kunal on 2/7/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.nano.kunal.moviemonkey.app";

    //The base URI used by all apps to contact the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "movie_details";
        //The columns in the table
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
    }



}
