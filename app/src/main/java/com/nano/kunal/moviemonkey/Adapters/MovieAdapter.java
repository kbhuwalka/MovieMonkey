package com.nano.kunal.moviemonkey.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.nano.kunal.moviemonkey.ui.MovieListFragment;
import com.nano.kunal.moviemonkey.R;
import com.nano.kunal.moviemonkey.Utilities;
import com.squareup.picasso.Picasso;

/**
 * Created by Kunal on 2/4/2016.
 */
public class MovieAdapter extends CursorAdapter {

    private static class ViewHolder{
        ImageView posterImageView;

        public ViewHolder(View view){
            posterImageView = (ImageView) view.findViewById(R.id.posterImageView);
        }
    }

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.movie_grid_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        String posterUrl = Utilities.getPosterUrl(
                cursor.getString(MovieListFragment.COL_POSTER_PATH),
                Utilities.IMG_SIZE_PATH);
        Picasso.with(context).load(posterUrl).into(holder.posterImageView);
    }
}
