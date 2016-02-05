package com.nano.kunal.moviemonkey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridItem;

        if(view == null){
            //If the view is not a recyc;ed one then initialize some attributes
            gridItem = new View(mContext);
            gridItem = inflater.inflate(R.layout.movie_grid_item_layout, null);
        } else{
            gridItem = (ImageView) view;
        }

        ImageView imageView = (ImageView) gridItem.findViewById(R.id.imageView);
        imageView.setImageResource(mThumbIds[i]);

        imageView.setImageResource(mThumbIds[i]);
        return imageView;
    }

    //Reference to sample Images
    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };
}
