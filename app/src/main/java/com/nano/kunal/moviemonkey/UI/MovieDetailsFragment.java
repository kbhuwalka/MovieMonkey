package com.nano.kunal.moviemonkey.ui;



import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.nano.kunal.moviemonkey.adapters.MediaAdapter;
import com.nano.kunal.moviemonkey.adapters.ReviewsAdapter;
import com.nano.kunal.moviemonkey.data.MovieContract;
import com.nano.kunal.moviemonkey.DetailsLoader;
import com.nano.kunal.moviemonkey.model.MediaObject;
import com.nano.kunal.moviemonkey.model.ReviewObject;
import com.nano.kunal.moviemonkey.R;
import com.nano.kunal.moviemonkey.Utilities;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kunal on 6/25/2016.
 */
public class MovieDetailsFragment extends Fragment {

    //Views present in the layout
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.summaryTextView)
    TextView summaryTextView;
    @BindView(R.id.ratingTextView)
    TextView ratingTextView;
    @BindView(R.id.releaseTextView)
    TextView releaseTextView;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    private View rootView;
    @BindView(R.id.noMediaTextView)
    TextView noMediaTextView;
    @BindView(R.id.noReviewsTextView)
    TextView noReviewsTextView;
    @BindView(R.id.favoriteIcon)
    com.like.LikeButton favoriteButton;

    private Uri mUri;

    @BindView(R.id.mediaRecyclerView)
    RecyclerView mediaRecycler;
    @BindView(R.id.reviewRecyclerView)
    RecyclerView reviewsRecycler;

    private RecyclerView.LayoutManager mediaLayoutManager;
    private RecyclerView.LayoutManager reviewsLayoutManager;

    private MediaAdapter mediaAdapter;
    private ReviewsAdapter reviewsAdapter;

    private DetailsLoader detailsLoader;
    private DetailsLoader mediaLoader;
    private DetailsLoader reviewsLoader;

    private long movieId;

    private static final int ASYNC_INSERT_FAVORITE_ID = 1;
    private static final int ASYNC_DELETE_FAVORITE_ID = 2;

    public static final String DETAIL_URI = "movie detail uri";

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
        ButterKnife.bind(this, rootView);

        mediaLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mediaAdapter = new MediaAdapter(getContext());
        mediaRecycler.setLayoutManager(mediaLayoutManager);
        mediaRecycler.setAdapter(mediaAdapter);


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

        String title = data.getString(Utilities.DetailsProjection.COL_TITLE);
            titleTextView.setText(title);

        String releaseDate = data.getString(Utilities.DetailsProjection.COL_RELEASE_DATE);
            releaseTextView.setText(releaseDate);

        String rating  = data.getString(Utilities.DetailsProjection.COL_RATING);
            ratingTextView.setText(rating+"/10");
            ratingBar.setRating(Float.parseFloat(rating)/2);

        String summary = data.getString(Utilities.DetailsProjection.COL_OVERVIEW);
            summaryTextView.setText(summary);

        final long movieId = data.getLong(Utilities.DetailsProjection.COL_MOVIE_ID);
        final Uri favoriteUri = MovieContract.FavoriteEntry.buildFavoritesUri(movieId);
        Cursor favoriteCursor = getActivity().getContentResolver().query(favoriteUri, null, null, null, null);
        if(favoriteCursor!=null && favoriteCursor.moveToFirst())
            favoriteButton.setLiked(true);

        final AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getActivity().getContentResolver()){};

        favoriteButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ContentValues values = new ContentValues();
                values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID, movieId);
                asyncQueryHandler.startInsert(
                        ASYNC_INSERT_FAVORITE_ID, null, MovieContract.FavoriteEntry.CONTENT_URI, values);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                asyncQueryHandler.startDelete(ASYNC_DELETE_FAVORITE_ID, null, favoriteUri, null, null);
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
