package com.nano.kunal.moviemonkey.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.nano.kunal.moviemonkey.data.MovieContract;
import com.nano.kunal.moviemonkey.Utilities;

import java.util.Vector;

/**
 * Created by Kunal on 6/28/2016.
 */
public class ReviewObject {
    private long movieId;
    private String author;
    private String content;

    public ReviewObject(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public ReviewObject(long movieId, String author, String content) {
        this.movieId = movieId;
        this.author = author;
        this.content = content;
    }

    public long getMovieId() {
        return movieId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public static ContentValues[] toContentValuesArray(ReviewObject[] reviews){
        Vector<ContentValues> values = new Vector<>();
        for (ReviewObject review:
             reviews) {
            ContentValues value = new ContentValues();
            value.put(MovieContract.ReviewsEntry.COLUMN_MOVIE_ID, review.getMovieId());
            value.put(MovieContract.ReviewsEntry.COLUMN_AUTHOR, review.getAuthor());
            value.put(MovieContract.ReviewsEntry.COLUMN_CONTENT, review.getContent());
            values.add(value);
        }
        return values.toArray(new ContentValues[values.size()]);
    }

    public static ReviewObject[] toArray(Cursor data) {
        Vector<ReviewObject> list = new Vector<>();
        do{
            list.add(new ReviewObject(
                    data.getString(Utilities.ReviewProjection.COL_AUTHOR),
                    data.getString(Utilities.ReviewProjection.COL_CONTENT)
            ));
        }while (data.moveToNext());
        return list.toArray(new ReviewObject[list.size()]);
    }
}
