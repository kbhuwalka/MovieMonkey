package com.nano.kunal.moviemonkey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private GridView movieGrid;
    private static final String PREFERENCES_FILE = "preferences";
    private String sortBy;
    private String sortOrder;

    @Override
    protected void onStart(){
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE, 0 );
        sortBy = sharedPreferences.getString(getString(R.string.sort_category_pref_key), getString(R.string.sort_category_value_popularity));
        sortOrder = sharedPreferences.getString(getString(R.string.sort_order_pref_key), getString(R.string.sort_order_value_asc));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieAdapter movieAdapter = new MovieAdapter(this);

        //Setting an Adapter to the grid
        movieGrid = (GridView) findViewById(R.id.movieGrid);
        movieGrid.setAdapter(movieAdapter);

        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "" + i, Toast.LENGTH_SHORT).show();
            }
        });

        FetchMoviesTask task = new FetchMoviesTask(movieAdapter, sortBy+sortOrder);
        task.execute();


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
