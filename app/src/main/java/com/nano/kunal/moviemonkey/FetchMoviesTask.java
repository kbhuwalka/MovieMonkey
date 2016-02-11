package com.nano.kunal.moviemonkey;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kunal on 2/5/2016.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... voids) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonString = "";

        final String BASE_URL = "http://api.themoviedb.org/3";
        final String DISCOVER_PATH = "discover";
        final String MOVIE_PATH = "movie";
        final String API_KEY_PARAM = "api_key";

        //Developer Api Key
        final String API_KEY = "85d62bbc194880880325544a0d180547";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(DISCOVER_PATH)
                        .appendPath(MOVIE_PATH)
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();

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

        return movieJsonString;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}
