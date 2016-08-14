package com.nano.kunal.moviemonkey;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.nano.kunal.moviemonkey.ui.MovieDetailsFragment;

/**
 * Created by Kunal on 6/27/2016.
 */
public class DetailsLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int DETAILS_LOADER_ID = 100;
    public static final int MEDIA_LOADER_ID = 200;
    public static final int REVIEWS_LOADER_ID = 300;

    private Uri mUri;
    private MovieDetailsFragment mFragment;

    public DetailsLoader(MovieDetailsFragment fragment, Uri uri){
        mFragment = fragment;
        mUri= uri;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case DETAILS_LOADER_ID:
               return new CursorLoader(mFragment.getContext(),
                        mUri,
                        Utilities.DetailsProjection.DETAILS_PROJECTION,
                        null,
                        null,
                        null);

            case MEDIA_LOADER_ID:
                return new CursorLoader(mFragment.getContext(),
                        mUri,
                        Utilities.MediaProjection.MEDIA_PROJECTION,
                        null,
                        null,
                        null);

            case REVIEWS_LOADER_ID:
                return new CursorLoader(mFragment.getContext(),
                        mUri,
                        Utilities.ReviewProjection.REVIEWS_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new UnsupportedOperationException("Unknown ID: " + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()){
            switch (loader.getId()){
                case DETAILS_LOADER_ID:
                    mFragment.renderDetails(data);
                    break;
                case MEDIA_LOADER_ID:
                    mFragment.renderMedia(data);
                    break;
                case REVIEWS_LOADER_ID:
                    mFragment.renderReviews(data);
                    break;
            }
        }
        Bundle taskBundle = new Bundle();
        taskBundle.putLong(FetchMoviesTask.MOVIE_ID_KEY, mFragment.getMovieId());

        new FetchMoviesTask(mFragment.getContext(), taskBundle, FetchMoviesTask.FETCH_MEDIA).execute();
        new FetchMoviesTask(mFragment.getContext(), taskBundle, FetchMoviesTask.FETCH_REVIEWS).execute();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
