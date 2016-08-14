package com.nano.kunal.moviemonkey.ui;



import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nano.kunal.moviemonkey.adapters.MovieAdapter;
import com.nano.kunal.moviemonkey.data.MovieContract;
import com.nano.kunal.moviemonkey.R;

import butterknife.BindView;

/**
 * Created by Kunal on 6/25/2016.
 */
public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    //Tag used for logging debug information
    public static final String LOG_TAG = MovieListFragment.class.getSimpleName();

    private GridView mGridView;
    private MovieAdapter movieAdapter;

    private static final String[] MOVIE_PROJECTION = {
            MovieContract.MovieEntry.TABLE_NAME+"."+MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.TABLE_NAME+"."+MovieContract.MovieEntry._ID
    };

    private static final int MOVIE_LOADER = 100;

    public static final int COL_MOVIE_ID =0 ;
    public static final int COL_TITLE = 1;
    public static final int COL_POSTER_PATH = 2;
    public static final int COL_POPULARITY = 3;
    public static final int COL_RATING = 4;
    public static final int COL_ID = 5;

    public static final String URI_KEY = " favorite_uri";

    private Uri mUri;

    /*
     *An interface that must be implemented by all activities that contain this fragment.
     * This will allow the activities to be notified of an item selection
     */
    public interface Callback{
        //Callback for when an item is selected
        public void onItemSelected(Uri uri);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args!=null && args.containsKey(URI_KEY)){
            mUri = Uri.parse(args.getString(URI_KEY));
        }else{
            mUri = MovieContract.MovieEntry.CONTENT_URI;
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.movie_list_fragment, container,false);
        mGridView = (GridView) rootView.findViewById(R.id.movieGrid);

        movieAdapter = new MovieAdapter(getActivity(),null,0);
        mGridView.setAdapter(movieAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if(cursor != null){
                    long movieId = cursor.getLong(MovieListFragment.COL_MOVIE_ID);
                    Uri detailUri = MovieContract.MovieEntry.buildMovieUri(movieId);
                    ((Callback)getActivity()).onItemSelected(detailUri);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER,null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        return new CursorLoader(getActivity(),
                mUri,
                MOVIE_PROJECTION, //Projection
                null,
                null,
                null); //sort Order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }
}
