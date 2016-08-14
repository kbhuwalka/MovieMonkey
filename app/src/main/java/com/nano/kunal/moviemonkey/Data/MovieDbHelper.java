package com.nano.kunal.moviemonkey.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Kunal on 6/25/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "moviesdatabase.db";
    public static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
         *CREATE TABLE movies (
         *   _id INTEGER PRIMARY KEY,
         *   movie_id INTEGER UNIQUE NOT NULL,
         *   title TEXT NOT NULL,
         *   release_date TEXT NOT NULL,
         *   overview TEXT NOT NULL,
         *   poster_path TEXT NOT NULL,
         *   backdrop_path TEXT NOT NULL,
         *   rating REAL NOT NULL,
         *   popularity REAL NOT NULL );
         */
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE "+ MovieContract.MovieEntry.TABLE_NAME+" (" +
                        MovieContract.MovieEntry._ID+" INTEGER PRIMARY KEY," +
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID +" INTEGER UNIQUE NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_TITLE +" TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE +" TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_OVERVIEW+ " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POSTER_PATH+ " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_BACKDROP_PATH+ " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RATING+ " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POPULARITY+ " REAL NOT NULL );"
        ;


        /*
         *CREATE TABLE genres(
         *_id INTEGER PRIMARY KEY,
         *genre_id INTEGER UNIQUE NOT NULL,
         *genre_name TEXT NOT NULL);
         */

        final String SQL_CREATE_GENRE_TABLE =
                "CREATE TABLE "+ MovieContract.GenresTable.TABLE_NAME+ "( " +
                        MovieContract.GenresTable._ID+ " INTEGER PRIMARY KEY, " +
                        MovieContract.GenresTable.COLUMN_GENRE_ID+ " INTEGER UNIQUE NOT NULL, " +
                        MovieContract.GenresTable.COLUMN_GENRE_NAME+ " TEXT NOT NULL);"
                ;

        /*
         *CREATE TABLE movies_genre (
         * _id INTEGER PRIMARY KEY,
         * movie_id INTEGER NOT NULL,
         * genre_id INTEGER NOT NULL,
         * FOREIGN KEY (movie_id)
         * REFERENCES movies(movie_id),
         * FOREIGN KEY (genre_id)
         * REFERENCES genres(genre_id)
         * );
        */
        final String SQL_CREATE_MOVIES_GENRE_TABLE =
                "CREATE TABLE "+ MovieContract.MoviesGenre.TABLE_NAME+ " (" +
                        MovieContract.MoviesGenre._ID+ " INTEGER PRIMARY KEY, " +
                        MovieContract.MoviesGenre.COLUMN_MOVIE_ID+ " INTEGER NOT NULL, " +
                        MovieContract.MoviesGenre.COLUMN_GENRE_ID+ " INTEGER NOT NULL, " +
                        "FOREIGN KEY ("+ MovieContract.MoviesGenre.COLUMN_MOVIE_ID+ ") " +
                        "REFERENCES "+ MovieContract.MovieEntry.TABLE_NAME+
                        " ("+ MovieContract.MovieEntry.COLUMN_MOVIE_ID+ "), " +
                        "FOREIGN KEY ("+ MovieContract.MoviesGenre.COLUMN_GENRE_ID+ ") " +
                        "REFERENCES "+ MovieContract.GenresTable.TABLE_NAME+
                        " ("+ MovieContract.GenresTable.COLUMN_GENRE_ID+") " +
                        ");";


        /*
         *CREATE TABLE favorites(
         * _id INTEGER PRIMARY KEY,
         * movie_id INTEGER UNIQUE NOT NULL,
         * FOREIGN KEY (movie_id)
         * REFERENCES movies(movie_id)
         *);
         */
        final String SQL_CREATE_FAVORITES_TABLE =
                "CREATE TABLE "+ MovieContract.FavoriteEntry.TABLE_NAME+"(" +
                        MovieContract.FavoriteEntry._ID+ " INTEGER PRIMARY KEY, " +
                        MovieContract.FavoriteEntry.COLUMN_MOVIE_ID+ " INTEGER UNIQUE NOT NULL, " +
                        "FOREIGN KEY ("+ MovieContract.FavoriteEntry.COLUMN_MOVIE_ID+") " +
                        "REFERENCES "+ MovieContract.MovieEntry.TABLE_NAME+
                        " ("+ MovieContract.MovieEntry.COLUMN_MOVIE_ID+") " +
                        ");";

        /*
         *   CREATE TABLE media(
         *       _id INTEGER PRIMARY KEY,
         *       movie_id INTEGER NOT NULL,
         *       media_type INTEGER NOT NULL,
         *       path TEXT UNIQUE NOT NULL,
         *       FOREIGN KEY (movie_id)
         *       REFERENCES movies(movie_id)
         *   );
         */
        final String SQL_CREATE_MEDIA_TABLE =
                "CREATE TABLE "+ MovieContract.MediaEntry.TABLE_NAME+ "(" +
                        MovieContract.MediaEntry._ID+ " INTEGER PRIMARY KEY, " +
                        MovieContract.MediaEntry.COLUMN_MOVIE_ID+ " INTEGER NOT NULL, " +
                        MovieContract.MediaEntry.COLUMN_MEDIA_TYPE+ " INTEGER NOT NULL, " +
                        MovieContract.MediaEntry.COLUMN_PATH+ " TEXT UNIQUE NOT NULL, " +
                        "FOREIGN KEY (" + MovieContract.MediaEntry.COLUMN_MOVIE_ID + ") " +
                        "REFERENCES "+ MovieContract.MovieEntry.TABLE_NAME+
                        " ("+ MovieContract.MovieEntry.COLUMN_MOVIE_ID+") "+
                        ");";

        /*
         *   CREATE TABLE reviews (
         *       _id INTEGER PRIMARY KEY,
         *       movie_id INTEGER NOT NULL,
         *       author TEXT NOT NULL,
         *       content TEXT UNIQUE NOT NULL,
         *       FOREIGN KEY (movie_id)
         *       REFERENCES movies
         *        (movie_id)
         *   );
         */

        final String SQL_CREATE_REVIEwS_TABLE = "" +
                "CREATE TABLE "+ MovieContract.ReviewsEntry.TABLE_NAME+ " (" +
                MovieContract.ReviewsEntry._ID+ " INTEGER PRIMARY KEY, " +
                MovieContract.ReviewsEntry.COLUMN_MOVIE_ID+ " INTEGER NOT NULL, " +
                MovieContract.ReviewsEntry.COLUMN_AUTHOR+ " TEXT NOT NULL, " +
                MovieContract.ReviewsEntry.COLUMN_CONTENT+ " TEXT UNIQUE NOT NULL," +
                "FOREIGN KEY (" + MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + ") " +
                "REFERENCES "+ MovieContract.MovieEntry.TABLE_NAME+
                " ("+ MovieContract.MovieEntry.COLUMN_MOVIE_ID+") "+
                ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_GENRE_TABLE);
        db.execSQL(SQL_CREATE_MOVIES_GENRE_TABLE);
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
        db.execSQL(SQL_CREATE_MEDIA_TABLE);
        db.execSQL(SQL_CREATE_REVIEwS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.GenresTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MoviesGenre.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoriteEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MediaEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewsEntry.TABLE_NAME);

        onCreate(db);
    }
}
