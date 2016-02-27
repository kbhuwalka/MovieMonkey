package com.nano.kunal.moviemonkey.Data;

import java.util.ArrayList;

/**
 * Created by Kunal on 2/24/2016.
 */
public class CommonUtilities {

    public static ArrayList<MovieObject> mMoviesArray = new ArrayList<MovieObject>();

    private static MovieObject movieExtra;

    public static void setMovieExtra(MovieObject movie){
        movieExtra = movie;
    }

    public static MovieObject getMovieExtra(){
        return movieExtra;
    }

}
