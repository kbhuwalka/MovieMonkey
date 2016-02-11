package com.nano.kunal.moviemonkey.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kunal on 2/7/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    public static final int DATBASE_VERSION = 1;

    public static final String DATABASE_NAME = "momo.db";

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATBASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL " +
                ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}