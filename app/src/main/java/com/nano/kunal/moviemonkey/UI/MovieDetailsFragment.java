package com.nano.kunal.moviemonkey.UI;



import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.nano.kunal.moviemonkey.Adapters.MediaAdapter;
import com.nano.kunal.moviemonkey.Adapters.ReviewsAdapter;
import com.nano.kunal.moviemonkey.Data.MovieContract;
import com.nano.kunal.moviemonkey.DetailsLoader;
import com.nano.kunal.moviemonkey.FetchMoviesTask;
import com.nano.kunal.moviemonkey.Model.MediaObject;
import com.nano.kunal.moviemonkey.Model.ReviewObject;
import com.nano.kunal.moviemonkey.R;
import com.nano.kunal.moviemonkey.Utilities;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Kunal on 6/25/2016.
 */
public class MovieDetailsFragment extends Fragment {

    //Views present in the layout
    private TextView titleTextView;
    private TextView summaryTextView;
    private TextView ratingTextView;
    private TextView releaseTextView;
    private View rootView;
    private TextView noMediaTextView;
    private TextView noReviewsTextView;
    private com.like.LikeButton favoriteButton;

    private Uri mUri;

    public static final String DETAIL_URI = "movie detail uri";

    private RecyclerView mediaRecycler;
    private RecyclerView reviewsRecycler;

    private RecyclerView.LayoutManager mediaLayoutManager;
    private RecyclerView.LayoutManager reviewsLayoutManager;

    private MediaAdapter mediaAdapter;
    private ReviewsAdapter reviewsAdapter;

    private DetailsLoader detailsLoader;
    private DetailsLoader mediaLoader;
    private DetailsLoader reviewsLoader;

    private long movieId;

    public long getMovieId() {
        return movieId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if(arguments != null){
            mUri = arguments.getParcelable(DETAIL_URI);
            movieId = MovieContract.MovieEntry.getMovieIdFromUri(mUri);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if(arguments != null) {
            detailsLoader = new DetailsLoader(this, mUri);

            Uri mediaUri = MovieContract.MediaEntry.buildMediaUri(movieId);
            mediaLoader = new DetailsLoader(this,mediaUri);

            Uri reviewsUri = MovieContract.ReviewsEntry.buildReviewsUri(movieId);
            reviewsLoader = new DetailsLoader(this, reviewsUri);
        }

        rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);

        titleTextView = (TextView) rootView.findViewById(R.id.titleTextView);
        summaryTextView = (TextView) rootView.findViewById(R.id.summaryTextView);
        ratingTextView = (TextView) rootView.findViewById(R.id.ratingTextView);
        releaseTextView = (TextView) rootView.findViewById(R.id.releaseTextView);
        favoriteButton = (LikeButton) rootView.findViewById(R.id.favoriteIcon);

        noMediaTextView = (TextView) rootView.findViewById(R.id.noMediaTextView);
        noReviewsTextView = (TextView) rootView.findViewById(R.id.noReviewsTextView);

        mediaRecycler = (RecyclerView) rootView.findViewById(R.id.mediaRecyclerView);
        mediaLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mediaAdapter = new MediaAdapter(getContext());
        mediaRecycler.setLayoutManager(mediaLayoutManager);
        mediaRecycler.setAdapter(mediaAdapter);


        reviewsRecycler = (RecyclerView) rootView.findViewById(R.id.reviewRecyclerView);
        reviewsLayoutManager = new LinearLayoutManager(getActivity());
        reviewsAdapter = new ReviewsAdapter();
        reviewsRecycler.setLayoutManager(reviewsLayoutManager);
        reviewsRecycler.setAdapter(reviewsAdapter);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if(getArguments()!= null){
            LoaderManager manager = getLoaderManager();
            manager.initLoader(DetailsLoader.DETAILS_LOADER_ID, null, detailsLoader);
            manager.initLoader(DetailsLoader.MEDIA_LOADER_ID, null, mediaLoader);
            manager.initLoader(DetailsLoader.REVIEWS_LOADER_ID, null, reviewsLoader);
        }



        super.onActivityCreated(savedInstanceState);

    }

    public void renderDetails(Cursor data) {
        String backdropUrl = Utilities.getBackdropUrl(data.getString(Utilities.DetailsProjection.COL_BACKDROP_PATH));
        // Picasso.with(getActivity()).load(backdropUrl).into(backdropImageView);

        Picasso.with(getActivity()).load(backdropUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                rootView.setBackground(
                        new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });


//      String posterUrl = Utilities.getPosterUrl(
//            data.getString(Utilities.DetailsProjection.COL_POSTER_PATH), Utilities.SMALL_IMG_SIZE_PATH);
//      Picasso.with(getActivity()).load(posterUrl).into(posterView);

        String title = data.getString(Utilities.DetailsProjection.COL_TITLE);
            titleTextView.setText(title);

        String releaseDate = data.getString(Utilities.DetailsProjection.COL_RELEASE_DATE);
            releaseTextView.setText(releaseDate);

        String rating  = data.getString(Utilities.DetailsProjection.COL_RATING);
            ratingTextView.setText(rating);

        String summary = data.getString(Utilities.DetailsProjection.COL_OVERVIEW);
            summaryTextView.setText(summary);

        final long movieId = data.getLong(Utilities.DetailsProjection.COL_MOVIE_ID);
        final Uri favoriteUri = MovieContract.FavoriteEntry.buildFavoritesUri(movieId);
        Cursor favoriteCursor = getActivity().getContentResolver().query(favoriteUri, null, null, null, null);
        if(favoriteCursor!=null && favoriteCursor.moveToFirst())
            favoriteButton.setLiked(true);

        favoriteButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ContentValues values = new ContentValues();
                values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID, movieId);
                getActivity().getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI, values);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                getActivity().getContentResolver().delete(favoriteUri, null, null);
            }
        });
    }

    public void renderMedia(Cursor data){
        MediaObject[] media = MediaObject.toArray(data);
        if(media.length != 0){
            mediaRecycler.setVisibility(View.VISIBLE);
            noMediaTextView.setVisibility(View.GONE);
            mediaAdapter.setData(media);
        }else {
            mediaRecycler.setVisibility(View.GONE);
            noMediaTextView.setVisibility(View.VISIBLE);
        }
    }

    public void renderReviews(Cursor data){
        ReviewObject[] reviews = ReviewObject.toArray(data);
        if(reviews.length != 0){
            reviewsRecycler.setVisibility(View.VISIBLE);
            noReviewsTextView.setVisibility(View.GONE);
            reviewsAdapter.setData(reviews);
        }else{
            reviewsRecycler.setVisibility(View.GONE);
            noReviewsTextView.setVisibility(View.VISIBLE);
        }

    }

}
