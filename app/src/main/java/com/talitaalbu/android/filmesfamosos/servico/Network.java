package com.talitaalbu.android.filmesfamosos.servico;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */


@SuppressWarnings("ALL")
public final class Network {

    public enum SearchType
    {
        POPULAR_MOVIE,
        TOP_RATED_MOVIE
    }

    private static final String INITIAL_URL = "https://api.themoviedb.org/3";

    private static final String POPULAR_MOVIE_URL =
            INITIAL_URL+ "/movie/popular?api_key=";

    private static final String TOP_RATED_MOVIE_URL =
            INITIAL_URL+ "/movie/top_rated?api_key=";

    private static final String API_KEY = "284e06ab86d0a46fcb041e055248e86b";
    private static final String TAG = "Network";


    public static URL buildUrl(SearchType type) {
        String URLvalue;
        if(type==SearchType.POPULAR_MOVIE)
        {
            URLvalue = POPULAR_MOVIE_URL;
        }
        else
        {
            URLvalue = TOP_RATED_MOVIE_URL;
        }
        Uri builtUri = Uri.parse(URLvalue+ API_KEY).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
