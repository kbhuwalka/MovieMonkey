package com.nano.kunal.moviemonkey.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nano.kunal.moviemonkey.model.ReviewObject;
import com.nano.kunal.moviemonkey.R;

/**
 * Created by Kunal on 6/27/2016.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private ReviewObject[] data;

    public ReviewsAdapter(){
        data = new ReviewObject[0];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReviewObject item = data[position];

        holder.authorTextView.setText(item.getAuthor());
        holder.contentTextView.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public void setData(ReviewObject[] array){
        data = array;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView authorTextView;
        public TextView contentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            authorTextView = (TextView) itemView.findViewById(R.id.authorTextView);
            contentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
        }
    }
}
