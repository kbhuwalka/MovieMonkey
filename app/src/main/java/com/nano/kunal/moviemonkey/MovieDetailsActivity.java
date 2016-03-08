package com.nano.kunal.moviemonkey;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nano.kunal.moviemonkey.Data.CommonUtilities;
import com.nano.kunal.moviemonkey.Data.MovieObject;
import com.squareup.picasso.Picasso;


public class MovieDetailsActivity extends AppCompatActivity {

    private MovieObject movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movie = CommonUtilities.getMovieExtra();

        View rootView = findViewById(R.id.movieDetailsRoot);

        ImageView backdrop = (ImageView) rootView.findViewById(R.id.backdropImageView);
        Picasso.with(this).load(movie.getBackdropUrl()).into(backdrop);

        ImageView poster = (ImageView) rootView.findViewById(R.id.posterImageView);
        Picasso.with(this).load(movie.getPosterUrl(MovieObject.SMALL_IMG_SIZE_PATH)).into(poster);

        TextView release = (TextView) rootView.findViewById(R.id.releaseTextView);
        release.setText(movie.getReleaseYear());

        TextView title = (TextView) rootView.findViewById(R.id.titleTextView);
        title.setText(movie.getOriginalTitle());

        TextView summary = (TextView) rootView.findViewById(R.id.summaryTextView);
        summary.setText(movie.getOverview());

        TextView rating = (TextView) rootView.findViewById(R.id.ratingTextView);
        rating.setText( movie.getVoteAverage() + "" );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
