package com.nano.kunal.moviemonkey.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nano.kunal.moviemonkey.FetchMoviesTask;
import com.nano.kunal.moviemonkey.R;

public class MainActivity extends AppCompatActivity implements MovieListFragment.Callback {

    public static final String MOVIE_DETAILS_EXTRA = "movie_details_extra";
    private static final String DETAIL_FRAGMENT_TAG = "Movie Detail Fragment";

    private String sortBy;
    private String sortOrder;
    private String previousSortsetting="";

    //Indicates whether a master-detail flow is being used or not
    boolean mTwoPaneLayout;

    @Override
    protected void onStart(){
        super.onStart();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sortBy = sharedPreferences.getString(getString(R.string.sort_category_pref_key), getString(R.string.sort_category_value_popularity));
        sortOrder = sharedPreferences.getString(getString(R.string.sort_order_pref_key), getString(R.string.sort_order_value_desc));

        if(!previousSortsetting.equals(sortBy+sortOrder))  {
            previousSortsetting = sortBy+sortOrder;
            updateMovies();
        }
    }

    private void updateMovies() {
        Bundle args = new Bundle();
        args.putString(FetchMoviesTask.SORT_KEY, sortBy+sortOrder);

        FetchMoviesTask task = new FetchMoviesTask(this,args,FetchMoviesTask.FETCH_MOVIES );
        task.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.movie_detail_container) != null){
            mTwoPaneLayout = true;

            if(savedInstanceState == null)
                getSupportFragmentManager().beginTransaction()
                .add(R.id.movie_detail_container, new MovieDetailsFragment(), DETAIL_FRAGMENT_TAG)
                .commit();
        } else {
            mTwoPaneLayout = false;
        }
    }

    //The method is called through an instance of MovieDetailsFragment when an item is clicked
    @Override
    public void onItemSelected(Uri uri) {
        if(mTwoPaneLayout){
            MovieDetailsFragment detailsFragment = new MovieDetailsFragment();

            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailsFragment.DETAIL_URI, uri);
            detailsFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, detailsFragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
