package com.nano.kunal.moviemonkey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.nano.kunal.moviemonkey.Data.CommonUtilities;
import com.nano.kunal.moviemonkey.Data.MovieObject;

public class MainActivity extends AppCompatActivity {

    private GridView movieGrid;

    public static final String MOVIE_DETAILS_EXTRA = "movie_details_extra";

    private MovieAdapter mMovieAdapter;

    private String sortBy;
    private String sortOrder;
    private String previousSortsetting="";

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
        FetchMoviesTask task = new FetchMoviesTask(mMovieAdapter, sortBy+sortOrder);
        task.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieAdapter = new MovieAdapter(this);

        //Setting an Adapter to the grid
        movieGrid = (GridView) findViewById(R.id.movieGrid);
        movieGrid.setAdapter(mMovieAdapter);

        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                CommonUtilities.setMovieExtra((MovieObject) mMovieAdapter.getItem(i));
                startActivity(intent);
            }
        });
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
