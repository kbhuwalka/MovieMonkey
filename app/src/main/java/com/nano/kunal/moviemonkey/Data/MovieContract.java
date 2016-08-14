package com.nano.kunal.moviemonkey.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Kunal on 6/25/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.nano.kunal.moviemonkey.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movies";
    public static final String PATH_GENRE = "genre";
    public static final String PATH_FAVORITE = "favorites";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_MEDIA = "media";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+ "/"+ CONTENT_AUTHORITY+PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE=
                ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/"+CONTENT_AUTHORITY+PATH_MOVIE;

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getMovieIdFromUri(Uri uri){
            return Long.parseLong(uri.getPathSegments().get(1));
        }

    }

    public static final class GenresTable implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+ "/"+ CONTENT_AUTHORITY+PATH_GENRE;
        public static final String CONTENT_ITEM_TYPE=
                ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/"+CONTENT_AUTHORITY+PATH_GENRE;

        public static final String TABLE_NAME = "genres";

        public static final String COLUMN_GENRE_ID = "genre_id";
        public static final String COLUMN_GENRE_NAME = "genre_name";

        public static Uri buildGenreUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getGenreIdFromUri(Uri uri){
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

    public static final class MoviesGenre implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE).appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME = "movies_genre";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_GENRE_ID = "genre_id";

        public static Uri buildGenreUriForMovie(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static Uri buildUriWithMovieandGenre(long movieId, long genreId){
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE)
                    .appendPath(Long.toString(movieId))
                    .appendPath(Long.toString(genreId)).build();
        }

        public static Long getMovieIdFromGenreUri(Uri uri){
            return Long.parseLong(uri.getPathSegments().get(2));
        }
    }

    public static final class FavoriteEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static Uri buildFavoritesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getMovieIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

    public static final class ReviewsEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+ "/"+ CONTENT_AUTHORITY+PATH_REVIEWS;

        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";

        public static Uri buildReviewsUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getMovieIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

    public static final class MediaEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDIA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+ "/"+ CONTENT_AUTHORITY+PATH_MEDIA;

        public static final String TABLE_NAME = "media";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_PATH = "path";
        public static final String COLUMN_MEDIA_TYPE = "media_type";

        public static Uri buildMediaUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMediaUriwithType(long id, int type){
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .appendPath(Integer.toString(type))
                    .build();
        }

        public static int getTypeFromUri(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(2));
        }

        public static long getMovieIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

}
