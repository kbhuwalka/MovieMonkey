package com.nano.kunal.moviemonkey;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.nano.kunal.moviemonkey.Data.MovieObject;

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
import java.util.Vector;

/**
 * Created by Kunal on 2/5/2016.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, MovieObject[]> {

    public static final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    private MovieAdapter mAdapter;
    private String sortQuery;
    private Context mContext;

    public FetchMoviesTask(Context context, MovieAdapter adapter, String sort){
        mContext = context;
        mAdapter = adapter;
        sortQuery = sort;
    }

    @Override
    protected MovieObject[] doInBackground(Void... voids) {

        if(!isNetworkAvailable()){
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonString = "";

        final String BASE_URL = "http://api.themoviedb.org/3";
        final String DISCOVER_PATH = "discover";
        final String MOVIE_PATH = "movie";
        final String API_KEY_PARAM = "api_key";
        final String SORT_BY_PARAM = "sort_by";

        //Developer Api Key
        final String API_KEY = "";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(DISCOVER_PATH)
                        .appendPath(MOVIE_PATH)
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .appendQueryParameter(SORT_BY_PARAM, sortQuery)
                        .build();

        Log.v("URL: ", builtUri.toString());

        try {

            //The url for connecting
            URL url = new URL(builtUri.toString());

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

            movieJsonString = buffer.toString();

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
        Log.d("JSON: ", movieJsonString);

        try {
            return getDataFromJson(movieJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = (activeNetwork!=null) && (activeNetwork.isConnectedOrConnecting());

        if(isConnected) return true;

        return false;
    }

    private MovieObject[] getDataFromJson(String jsonStr) throws JSONException {

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

        Vector<MovieObject> movieVector = new Vector<MovieObject>();

        for(int i = 0; i < results.length(); i++ ){

            JSONObject movie = results.getJSONObject(i);
            movieVector.add( new MovieObject(
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
        if( movieVector.size() > 0 ){
            movieArray = new MovieObject[movieVector.size()];
            movieVector.toArray(movieArray);
        }

        for(int i = 0; i < movieArray.length; i++ )
            Log.v(LOG_TAG, movieArray[i].toString());

        return movieArray;
    }
    @Override
    protected void onPostExecute(MovieObject[] movieArray) {
        super.onPostExecute(movieArray);
        if(movieArray != null){
            MovieAdapter.mMoviesArray.clear();
            for(MovieObject item : movieArray)
                MovieAdapter.mMoviesArray.add(item);
            mAdapter.notifyDataSetChanged();
        }
    }
}
