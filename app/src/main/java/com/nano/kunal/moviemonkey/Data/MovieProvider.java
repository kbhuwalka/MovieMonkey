package com.nano.kunal.moviemonkey.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Kunal on 6/25/2016.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private MovieDbHelper dbHelper;

    private static final SQLiteQueryBuilder moviesGenreQueryBuilder;
    private static final SQLiteQueryBuilder favoriteMoviesQueryBuilder;


    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;
    static final int GENRE = 102;
    static final int GENRE_WITH_ID = 103;
    static final int GENRE_WITH_MOVIE = 104;
    static final int GENRE_WITH_MOVIE_AND_ID = 105;
    static final int FAVORITE = 106;
    static final int FAVORITE_WITH_ID = 107;
    static final int MEDIA = 108;
    static final int MEDIA_WITH_ID = 109;
    static final int MEDIA_WITH_ID_AND_TYPE = 110;
    static final int REVIEWS = 111;
    static final int REVIEWS_WITH_ID = 112;

    static {
        moviesGenreQueryBuilder = new SQLiteQueryBuilder();
        /*
         *The query formatted below is:
         * movies INNER JOIN movies_genre ON movies.movie_id=movies_genre.movie_id
         * INNER JOIN genres ON movies_genre.genre_id=genres.genre_id
         */
        moviesGenreQueryBuilder.setTables(
                String.format("%s INNER JOIN %s ON %s.%s=%s.%s INNER JOIN %s ON %s.%s=%s.%s",
                        MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MoviesGenre.TABLE_NAME,
                        MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                        MovieContract.GenresTable.TABLE_NAME,
                        MovieContract.MoviesGenre.COLUMN_MOVIE_ID,
                        MovieContract.GenresTable.TABLE_NAME,
                        MovieContract.MoviesGenre.TABLE_NAME,
                        MovieContract.MoviesGenre.COLUMN_GENRE_ID,
                        MovieContract.GenresTable.TABLE_NAME,
                        MovieContract.GenresTable.COLUMN_GENRE_ID)
        );

        favoriteMoviesQueryBuilder = new SQLiteQueryBuilder();
        /*
         *The query formatted below is:
         * movies INNER JOIN favorites
         * ON movies.movie_id=favorites.movie_id
         */
        favoriteMoviesQueryBuilder.setTables(
                String.format("%s INNER JOIN %s ON %s.%s=%s.%s",
                        MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.FavoriteEntry.TABLE_NAME,
                        MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                        MovieContract.FavoriteEntry.TABLE_NAME,
                        MovieContract.FavoriteEntry.COLUMN_MOVIE_ID)
        );
    }

    private static final String favoriteMovieList = String.format("SELECT %s.%s FROM %s",
            MovieContract.FavoriteEntry.TABLE_NAME,
            MovieContract.FavoriteEntry.COLUMN_MOVIE_ID,
            MovieContract.FavoriteEntry.TABLE_NAME);

    //movie.movie_id = ?
    private static String movieSelectionById = String.format("%s.%s = ?",
            MovieContract.MovieEntry.TABLE_NAME,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID);

    //genre.genre_id = ?
    private static String genreSelectionById = String.format("%s.%s = ?",
            MovieContract.GenresTable.TABLE_NAME,
            MovieContract.GenresTable.COLUMN_GENRE_ID);

    //media.movie_id = ?
    private static String mediaSelectionById = String.format("%s.%s = ?",
            MovieContract.MediaEntry.TABLE_NAME,
            MovieContract.MediaEntry.COLUMN_MOVIE_ID);

    //favorites.movie_id = ?
    private static String favoriteSelectionById = String.format("%s.%s = ?",
            MovieContract.FavoriteEntry.TABLE_NAME,
            MovieContract.FavoriteEntry.COLUMN_MOVIE_ID);

    //reviews.movie_id = ?
    private static String reviewsSelectionById = String.format("%s.%s = ?",
            MovieContract.ReviewsEntry.TABLE_NAME,
            MovieContract.ReviewsEntry.COLUMN_MOVIE_ID);

    //media.movie_id = ? AND media.media_type = ?
    private static String mediaSelectionByIdAndType = String.format("%s.%s = ? AND %s.%s = ?",
            MovieContract.MediaEntry.TABLE_NAME,
            MovieContract.MediaEntry.COLUMN_MOVIE_ID,
            MovieContract.MediaEntry.TABLE_NAME,
            MovieContract.MediaEntry.COLUMN_MEDIA_TYPE);

    //movies_genre.movie_id = ?
    private static String movieGenreSelectionById = String.format("%s.%s = ?",
            MovieContract.MoviesGenre.TABLE_NAME,
            MovieContract.MoviesGenre.COLUMN_MOVIE_ID);

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        String[] uris = {
                MovieContract.PATH_MOVIE,   // Uri - /movie
                MovieContract.PATH_MOVIE+"/#", //Uri - /movie/#
                MovieContract.PATH_GENRE,   //Uri - /genre
                MovieContract.PATH_GENRE+"/#", //Uri - /genre/#
                MovieContract.PATH_GENRE+"/"+MovieContract.PATH_MOVIE+"/#", //Uri - /genre/movie/#
                MovieContract.PATH_GENRE+"/#/#",    //Uri - /genre/#/#
                MovieContract.PATH_FAVORITE,    //Uri - /favorite
                MovieContract.PATH_FAVORITE+"/#",   //Uri - /favorite/#
                MovieContract.PATH_MEDIA,   //Uri - /media
                MovieContract.PATH_MEDIA+"/#",  //Uri - /media/#
                MovieContract.PATH_MEDIA+"/#/#",    //Uri - /media/#/#
                MovieContract.PATH_REVIEWS, //Uri - /reviews
                MovieContract.PATH_REVIEWS+"/#" //Uri - /reviews/#
        };

        int[] matches = {
                MOVIE,
                MOVIE_WITH_ID,
                GENRE,
                GENRE_WITH_ID,
                GENRE_WITH_MOVIE,
                GENRE_WITH_MOVIE_AND_ID,
                FAVORITE,
                FAVORITE_WITH_ID,
                MEDIA,
                MEDIA_WITH_ID,
                MEDIA_WITH_ID_AND_TYPE,
                REVIEWS,
                REVIEWS_WITH_ID
        };

        for(int i=0; i < uris.length; i++)
            matcher.addURI(authority,uris[i], matches[i]);

        return matcher;
    }

    //Return the rows of genres and movies which match the given movie_id
    private Cursor getGenresByMovieId(Uri uri, String[] projection, String sortOrder) {
        String[] selectionArgs = {Long.toString(MovieContract.MoviesGenre.getMovieIdFromGenreUri(uri))};
        Cursor cursor = moviesGenreQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                movieSelectionById,
                selectionArgs,
                null,
                null,
                sortOrder
                );
        return cursor;
    }

    private Cursor getFavoriteByMovieId(Uri uri, String[] projection, String sortOrder) {
        String[] selectionArgs = {Long.toString(MovieContract.FavoriteEntry.getMovieIdFromUri(uri))};

        Cursor cursor = favoriteMoviesQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                favoriteSelectionById,
                selectionArgs,
                null,
                null,
                sortOrder);
        return cursor;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match){
            case FAVORITE:
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;

            case FAVORITE_WITH_ID:
            case MOVIE_WITH_ID:
                return  MovieContract.MovieEntry.CONTENT_ITEM_TYPE;

            case GENRE:
            case GENRE_WITH_MOVIE:
                return MovieContract.GenresTable.CONTENT_TYPE;

            case GENRE_WITH_ID:
            case GENRE_WITH_MOVIE_AND_ID:
                return MovieContract.GenresTable.CONTENT_ITEM_TYPE;

            case MEDIA:
            case MEDIA_WITH_ID:
            case MEDIA_WITH_ID_AND_TYPE:
                return MovieContract.MediaEntry.CONTENT_TYPE;

            case REVIEWS:
            case REVIEWS_WITH_ID:
                return MovieContract.ReviewsEntry.CONTENT_TYPE;

            default: throw new UnsupportedOperationException("Unknown Uri: "+ uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(uri == null)
            return null;
        int match = uriMatcher.match(uri);
        String tableName;

        switch (match){
            case MOVIE:{
                tableName = MovieContract.MovieEntry.TABLE_NAME;
                break;
            }

            case MOVIE_WITH_ID:{
                tableName = MovieContract.MovieEntry.TABLE_NAME;
                selection = movieSelectionById;
                selectionArgs = new String[]
                        { Long.toString(MovieContract.MovieEntry.getMovieIdFromUri(uri))};
                break;
            }

            case GENRE:{
                tableName = MovieContract.GenresTable.TABLE_NAME;
                break;
            }

            case GENRE_WITH_ID:{
                tableName = MovieContract.GenresTable.TABLE_NAME;
                selection = genreSelectionById;
                selectionArgs = new String[]
                        {Long.toString(MovieContract.GenresTable.getGenreIdFromUri(uri))};
                break;
            }

            case GENRE_WITH_MOVIE:{
                return getGenresByMovieId(uri, projection, sortOrder);
            }

            case FAVORITE: {
                return favoriteMoviesQueryBuilder.query(dbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            }

            case FAVORITE_WITH_ID: {
                return getFavoriteByMovieId(uri, projection, sortOrder);
            }
            case REVIEWS_WITH_ID:{
                tableName = MovieContract.ReviewsEntry.TABLE_NAME;
                selection = reviewsSelectionById;
                selectionArgs = new String[]{
                        Long.toString(MovieContract.ReviewsEntry.getMovieIdFromUri(uri))
                };
                break;
            }
            case MEDIA_WITH_ID: {
                tableName = MovieContract.MediaEntry.TABLE_NAME;
                break;
            }
            case MEDIA_WITH_ID_AND_TYPE: {
                tableName = MovieContract.MediaEntry.TABLE_NAME;
                selection=mediaSelectionByIdAndType;
                selectionArgs = new String[]
                        {Long.toString(MovieContract.MediaEntry.getMovieIdFromUri(uri)),
                         Integer.toString(MovieContract.MediaEntry.getTypeFromUri(uri))};
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+ uri);
        }

        Cursor returnCursor = dbHelper.getReadableDatabase().query(
                tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        return returnCursor;
    }



    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri = null;

        switch(match){
            case MOVIE:{
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = MovieContract.MovieEntry.CONTENT_URI;
                } else {
                    throw new SQLException(String.format("Failed to insert row into %s table.",
                            MovieContract.MovieEntry.TABLE_NAME));
                }
                break;
            }
            case GENRE:{
                long _id = db.insert(MovieContract.GenresTable.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = MovieContract.GenresTable.CONTENT_URI;
                } else {
                    throw new SQLException(String.format("Failed to insert row into %s table.",
                            MovieContract.GenresTable.TABLE_NAME));
                }
                break;
            }
            case FAVORITE:{
                long _id = db.insert(MovieContract.MoviesGenre.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = MovieContract.MoviesGenre.CONTENT_URI;
                } else {
                    throw new SQLException(String.format("Failed to insert row into %s table.",
                            MovieContract.MoviesGenre.TABLE_NAME));
                }
                break;
            }
            case MEDIA:{
                long _id = db.insert(MovieContract.MediaEntry.TABLE_NAME, null, values);
                if(_id != -1)
                    returnUri = MovieContract.MovieEntry.CONTENT_URI;
                else {
                    Log.e("Database", String.format("Failed to insert into %s table.",
                            MovieContract.MediaEntry.TABLE_NAME));
                }
                break;
            }
            case REVIEWS:{
                long _id = db.insert(MovieContract.ReviewsEntry.TABLE_NAME, null, values);
                if(_id != -1)
                    returnUri = MovieContract.ReviewsEntry.CONTENT_URI;
                else
                    Log.e("Database", String.format("Failed to insert into %s table.",
                            MovieContract.ReviewsEntry.TABLE_NAME));
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted = 0;

        switch (match){
            case MOVIE:{
                deleteDataFromTable(MovieContract.MovieEntry.TABLE_NAME);
                deleteDataFromTable(MovieContract.MoviesGenre.TABLE_NAME);
                deleteDataFromTable(MovieContract.MediaEntry.TABLE_NAME);
                deleteDataFromTable(MovieContract.ReviewsEntry.TABLE_NAME);
                break;
            }
            case GENRE_WITH_MOVIE:{
                selection = movieGenreSelectionById;
                selectionArgs = new String[]{
                  Long.toString(MovieContract.MoviesGenre.getMovieIdFromGenreUri(uri))
                };
                rowsDeleted = db.delete(MovieContract.MoviesGenre.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case FAVORITE:{
                rowsDeleted = db.delete(MovieContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case FAVORITE_WITH_ID:{
                selection = favoriteSelectionById;
                selectionArgs = new String[]{
                  Long.toString(MovieContract.FavoriteEntry.getMovieIdFromUri(uri))
                };
                rowsDeleted = db.delete(MovieContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MEDIA:{
                rowsDeleted = db.delete(MovieContract.MediaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case REVIEWS:{
                rowsDeleted = db.delete(MovieContract.ReviewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case MOVIE:{
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case GENRE:{
                rowsUpdated = db.update(MovieContract.GenresTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case FAVORITE:{
                rowsUpdated = db.update(MovieContract.FavoriteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case MEDIA:{
                rowsUpdated = db.update(MovieContract.MediaEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case REVIEWS:{
                rowsUpdated = db.update(MovieContract.ReviewsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }

        if(rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    private void deleteDataFromTable(String table){
        /*
        DELETE * FROM table
        WHERE table.movie_id
        NOT IN (
            SELECT favorites.movie_id
            FROM favorites
            )
     */
        String notFavoritedMovieSelection = String.format(
                "DELETE FROM %s WHERE %s.%s NOT IN ( %s )",
                table,
                table,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                favoriteMovieList
        );

        dbHelper.getWritableDatabase().execSQL(notFavoritedMovieSelection);

    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int count = 0;

        switch (match){
            case MOVIE:{
                db.beginTransaction();
                try {
                    for(ContentValues value: values){
                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,value);
                        if(_id != -1)
                            count++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            }
            case GENRE:{
                db.beginTransaction();
                try {
                    for(ContentValues value: values){
                        long _id = db.insert(MovieContract.GenresTable.TABLE_NAME,null,value);
                        if(_id != -1)
                            count++;
                    }
                } finally {
                    db.endTransaction();
                }
                break;
            }
            default:
                return super.bulkInsert(uri, values);
        }
        return count;
    }
}
