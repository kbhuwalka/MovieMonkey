package com.nano.kunal.moviemonkey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nano.kunal.moviemonkey.Data.MovieObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kunal on 2/4/2016.
 */
public class MovieAdapter extends BaseAdapter {

    private Context mContext;

    public MovieAdapter(Context c){
        mContext = c;
    }

    @Override
    public int getCount() {
        return mMoviesArray.size();
    }

    @Override
    public Object getItem(int i) {
        return mMoviesArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View returnView, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridItem;

        if(returnView == null){
            //If the view is not a recycled one then initialize some attributes
            gridItem = inflater.inflate(R.layout.movie_grid_item_layout, null);
        } else{
            gridItem = (ImageView) returnView;
        }

        ImageView imageView = (ImageView) gridItem.findViewById(R.id.imageView);

        Picasso.with(mContext).load(mMoviesArray.get(i).getPosterUrl()).into(imageView);

        return imageView;
    }

    public static ArrayList<MovieObject> mMoviesArray = new ArrayList<MovieObject>();
}
