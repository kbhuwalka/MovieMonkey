package com.nano.kunal.moviemonkey.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.nano.kunal.moviemonkey.data.MovieContract;
import com.nano.kunal.moviemonkey.Utilities;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Kunal on 6/27/2016.
 */
public class MediaObject {

    public static final int TYPE_POSTER = Utilities.MEDIA_TYPE_POSTER;
    public static final int TYPE_BACKDROP = Utilities.MEDIA_TYPE_BACKDROP;
    public static final int TYPE_VIDEO = Utilities.MEDIA_TYPE_VIDEO;

    private long mMovieId;
    private int mType;
    private String mPath;

    public MediaObject(int type, String path) {
        mType=type;
        mPath=path;
    }

    public MediaObject(long movieId, int type, String path){
        mMovieId = movieId;
        mType = type;
        mPath = path;
    }

    public long getMovieId() {
        return mMovieId;
    }

    public int getType() {
        return mType;
    }

    public String getPath() {
        return mPath;
    }

    public static ContentValues[] toContentValuesArray(MediaObject[] mediaList) {
        Vector<ContentValues> values = new Vector<>();
        for(MediaObject media: mediaList){
            ContentValues value = new ContentValues();
            value.put(MovieContract.MediaEntry.COLUMN_MOVIE_ID, media.getMovieId());
            value.put(MovieContract.MediaEntry.COLUMN_MEDIA_TYPE, media.getType());
            value.put(MovieContract.MediaEntry.COLUMN_PATH, media.getPath());
            values.add(value);
        }

        return values.toArray(new ContentValues[values.size()]);
    }

    public static MediaObject[] toArray(Cursor data) {
        ArrayList<MediaObject> list = new ArrayList<>();
        do{
            list.add(new MediaObject(
               data.getInt(Utilities.MediaProjection.COL_TYPE),
               data.getString(Utilities.MediaProjection.COL_PATH)
            ));
        }while (data.moveToNext());

        return list.toArray(new MediaObject[list.size()]);

    }
}
