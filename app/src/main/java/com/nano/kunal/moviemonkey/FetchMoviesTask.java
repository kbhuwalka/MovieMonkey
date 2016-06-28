package com.nano.kunal.moviemonkey;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

import com.nano.kunal.moviemonkey.Model.MediaObject;
import com.nano.kunal.moviemonkey.Data.MovieContract;
import com.nano.kunal.moviemonkey.Model.MovieObject;
import com.nano.kunal.moviemonkey.Model.ReviewObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Kunal on 2/5/2016.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, Void> {

    public static final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    final String BASE_URL = "http://api.themoviedb.org/3";
    final String DISCOVER_PATH = "discover";
    final String MOVIE_PATH = "movie";
    final String IMAGES_PATH = "images";
    final String VIDEOS_PATH = "videos";
    final String REVIEWS_PATH = "reviews";
    final String API_KEY_PARAM = "api_key";
    final String SORT_BY_PARAM = "sort_by";

    //Developer Api Key
    final String API_KEY = Utilities.API_KEY;

    public static final int FETCH_MOVIES = 1;
    public static final int FETCH_MEDIA = 2;
    public static final int FETCH_REVIEWS = 3;

    public static final String SORT_KEY = "sort_key";
    public static final String MOVIE_ID_KEY = "movie_id_key";

    final int TASK_ID;

    private Bundle arguments;
    private Context mContext;

    public FetchMoviesTask(Context context,Bundle args,int taskId){
        TASK_ID = taskId;
        mContext = context;
        arguments = args;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Debug.waitForDebugger();

        if(!isNetworkAvailable()){
            return null;
        }

        switch (TASK_ID){
            case FETCH_MOVIES:{
                fetchMovies();
                break;
            }
            case FETCH_MEDIA:{
                fetchMedia();
                break;
            }
            case FETCH_REVIEWS:{
                fetchReviews();
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown task ID: "+ TASK_ID);

        }

        return null;
    }

    private void fetchMovies() {
        String sortQuery = arguments.getString(SORT_KEY);
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(DISCOVER_PATH)
                .appendPath(MOVIE_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(SORT_BY_PARAM, sortQuery)
                .build();

        Log.v("URL: ", uri.toString());

        String moviesResult = getDatafromUrl(uri.toString());

        try {
            parseMoviesResult(moviesResult);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "fetchMovies: Error parsing movie details." );
        }
    }

    private void parseMoviesResult(String jsonStr) throws JSONException {

        final String JSON_RESULTS = "results";
        final String JSON_ID = "id";
        final String JSON_TITLE = "original_title";
        final String JSON_RELEASE_DATE = "release_date";
        final String JSON_POPULARITY = "popularity";
        final String JSON_VOTE_AVERAGE = "vote_average";
        final String JSON_OVERVIEW = "overview";
        final String JSON_POSTER_PATH = "poster_path";
        final String JSON_BACKDROP_PATH = "backdrop_path";


        //The enclosing JSON object
        JSONObject moviesJson = new JSONObject(jsonStr);
        //Array of movies from results object
        JSONArray results = moviesJson.getJSONArray(JSON_RESULTS);

        ArrayList<MovieObject> movieList = new ArrayList<>();

        for(int i = 0; i < results.length(); i++ ){

            JSONObject movie = results.getJSONObject(i);
            movieList.add( new MovieObject(
                    movie.getString(JSON_TITLE),
                    movie.getString(JSON_RELEASE_DATE),
                    movie.getString(JSON_OVERVIEW),
                    movie.getString(JSON_POSTER_PATH),
                    movie.getString(JSON_BACKDROP_PATH),
                    movie.getDouble(JSON_VOTE_AVERAGE),
                    movie.getDouble(JSON_POPULARITY),
                    movie.getLong(JSON_ID)
            ) );
        }

        MovieObject[] movieArray = null;
        if( movieList.size() > 0 ){
            movieArray = movieList.toArray(new MovieObject[movieList.size()]);
        }

        ContentValues[] values = MovieObject.toContentValuesArray(movieArray);

        ContentResolver resolver = mContext.getContentResolver();

        resolver.delete(MovieContract.MovieEntry.CONTENT_URI, null, null);

        int rowsInserted =resolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI, values);
        resolver.notifyChange(MovieContract.MovieEntry.CONTENT_URI, null);
        Log.d(LOG_TAG, "parseMoviesResult: "+ rowsInserted+" rows inserted.");
    }

    private void fetchReviews() {
        if(!arguments.containsKey(MOVIE_ID_KEY))
            return;
        long id = arguments.getLong(MOVIE_ID_KEY);
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(Long.toString(id))
                .appendPath(REVIEWS_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        String reviewsResult = getDatafromUrl(uri.toString());
        try {
            parseReviewsResult(reviewsResult, id);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "fetchReviews: Could not parse reviews.");
        }

    }

    private void parseReviewsResult(String reviewsResult, long movieId) throws JSONException {
        final String JSON_RESULTS = "results";
        final String JSON_AUTHOR = "author";
        final String JSON_CONTENT = "content";

        JSONObject results = new JSONObject(reviewsResult);
        JSONArray reviews = results.getJSONArray(JSON_RESULTS);

        ArrayList<ReviewObject> reviewsList = new ArrayList<>();
        for (int i = 0; i < reviews.length(); i++) {
            JSONObject review = reviews.getJSONObject(i);
            reviewsList.add(new ReviewObject(
                    movieId,
                    review.getString(JSON_AUTHOR),
                    review.getString(JSON_CONTENT)
            ));
        }
        if(reviewsList.size() >0) {
            ReviewObject[] reviewsArray = reviewsList.toArray(new ReviewObject[reviewsList.size()]);
            ContentValues[] values = ReviewObject.toContentValuesArray(reviewsArray);
            mContext.getContentResolver().bulkInsert(MovieContract.ReviewsEntry.CONTENT_URI, values);
            mContext.getContentResolver().notifyChange(MovieContract.ReviewsEntry.CONTENT_URI, null);
        }
    }

    private void fetchMedia() {
        if(!arguments.containsKey(MOVIE_ID_KEY))
            return;
        long id = arguments.getLong(MOVIE_ID_KEY);
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(Long.toString(id))
                .appendPath(IMAGES_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        String imagesResult = getDatafromUrl(uri.toString());

        uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(Long.toString(id))
                .appendPath(VIDEOS_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        String videosResult = getDatafromUrl(uri.toString());

        try{
            if (videosResult != null)
                parseVideoResult(videosResult, id);
            if(imagesResult != null)
                parseImageResult(imagesResult, id);
        }catch (JSONException e){
            Log.e(LOG_TAG, "fetchMedia: Error parsing media information");
        }

    }

    private void parseVideoResult(String videosResult, long movieId) throws JSONException {
        final String JSON_RESULTS = "results";
        final String JSON_KEY = "key";

        JSONObject result = new JSONObject(videosResult);
        JSONArray videos = result.getJSONArray(JSON_RESULTS);
        ArrayList<MediaObject> mediaList = new ArrayList<>();

        for(int i=0; i < videos.length(); i++){
            JSONObject media = videos.getJSONObject(i);
            mediaList.add(new MediaObject(
                    movieId,
                    MediaObject.TYPE_BACKDROP,
                    media.getString(JSON_KEY)
            ));
        }
        persistMediaData(mediaList.toArray(new MediaObject[mediaList.size()]));

    }

    private void parseImageResult(String imagesResult, long movieId) throws JSONException {
        final String JSON_BACKDROPS = "backdrops";
        final String JSON_POSTERS = "posters";
        final String JSON_FILE_PATH = "file_path";

        JSONObject result = new JSONObject(imagesResult);
        JSONArray backdrops = result.getJSONArray(JSON_BACKDROPS);
        JSONArray posters = result.getJSONArray(JSON_POSTERS);

        ArrayList<MediaObject> mediaList = new ArrayList<>();

        for(int i=0; i < backdrops.length(); i++){
            JSONObject media = backdrops.getJSONObject(i);
            mediaList.add(new MediaObject(
                    movieId,
                    MediaObject.TYPE_BACKDROP,
                    media.getString(JSON_FILE_PATH)
            ));
        }
        for (int i = 0; i < posters.length(); i++) {
            JSONObject media = posters.getJSONObject(i);
            mediaList.add(new MediaObject(
                    movieId,
                    MediaObject.TYPE_POSTER,
                    media.getString(JSON_FILE_PATH)
            ));
        }

        persistMediaData(mediaList.toArray(new MediaObject[mediaList.size()]));
    }

    private void persistMediaData(MediaObject[] mediaList) {
        if(mediaList == null)
            return;
        ContentValues[] values = MediaObject.toContentValuesArray(mediaList);

        mContext.getContentResolver().bulkInsert(MovieContract.MediaEntry.CONTENT_URI, values);
    }

    private String getDatafromUrl(String urlString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String outputString = null;

        try {

            //The url for connecting
            URL url = new URL(urlString);

            //Create a connection and connect to the moviedb
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read the input string
            InputStream inputStream =urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            //If no inputStream was returned, no point of parsing
            if(inputStream == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));

            //Read all the lines on the input stream
            String line;
            while((line = reader.readLine()) != null){
                buffer.append(line+"\n");
            }

            if(buffer.length() == 0)
                return null;

            outputString = buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();

            if(reader != null){
                try{
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return outputString;
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = (activeNetwork!=null) && (activeNetwork.isConnectedOrConnecting());

        if(isConnected) return true;

        return false;
    }


    @Override
    protected void onPostExecute(Void aVoid) {

    }
}
