package com.nano.kunal.moviemonkey.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nano.kunal.moviemonkey.model.MediaObject;
import com.nano.kunal.moviemonkey.R;
import com.nano.kunal.moviemonkey.Utilities;
import com.squareup.picasso.Picasso;

/**
 * Created by Kunal on 6/27/2016.
 */
public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {

    private final Context mContext;
    private MediaObject[] data;

    public MediaAdapter(Context context){
        mContext = context;
        data = new MediaObject[0];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_item_layout, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String url = "";
        final MediaObject item = data[position];
        int itemType = item.getType();
        switch (itemType){
            case Utilities.MEDIA_TYPE_BACKDROP:
                url = Utilities.getBackdropUrl(item.getPath());
                break;
            case Utilities.MEDIA_TYPE_POSTER:
                url = Utilities.getPosterUrl(item.getPath(), Utilities.IMG_SIZE_PATH);
                break;
            case Utilities.MEDIA_TYPE_VIDEO:
                url =  Utilities.getThumbnailUrl(item.getPath());
                break;
        }
        Picasso.with(mContext).load(url).into(holder.imageView);

        if(itemType == Utilities.MEDIA_TYPE_VIDEO){
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String videoUrl = String.format("https://www.youtube.com/watch?v=%s", item.getPath());
                    Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                    if(playVideoIntent.resolveActivity(mContext.getPackageManager())!=null)
                        mContext.startActivity(playVideoIntent);
                }
            });
        }
        else {
            holder.imageView.setOnClickListener(null);
        }

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public void setData(MediaObject[] array){
        data = array;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.mediaImageView);
        }
    }

}
