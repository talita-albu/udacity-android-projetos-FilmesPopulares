package com.talitaalbu.android.filmesfamosos.servico;

import com.talitaalbu.android.filmesfamosos.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */

public final class MoviesParseJson {

    public static ArrayList<Movie> getMoviesFromJson(String jsonStr)
            throws JSONException {

        final String IDENTIFIER_ID = "id";
        final String IDENTIFIER_POSTER_PATH = "poster_path";
        final String IDENTIFIER_ORIGINAL_TITLE = "original_title";
        final String IDENTIFIER_OVERVIEW = "overview";
        final String IDENTIFIER_VOTE_AVERAGE = "vote_average";
        final String IDENTIFIER_RELEASE_DATE = "release_date";
        final String IDENTIFIER_IMAGE = "http://image.tmdb.org/t/p/w500";
        final String IDENTIFIER_BACKDROP_PATH = "backdrop_path";

        ArrayList<Movie> movies;

        JSONObject forecastJson = new JSONObject(jsonStr);

        JSONArray movieArray = forecastJson.getJSONArray("results");

        movies = new ArrayList<>();

        if (movieArray.length() == 0) {
            return null;
        }

        for (int i = 0; i < movieArray.length(); i++) {
            /* Get the JSON object representing the day */
            JSONObject movie = movieArray.getJSONObject(i);
            Movie m = new Movie();
            m.setPosterPath(IDENTIFIER_IMAGE+ movie.getString(IDENTIFIER_POSTER_PATH));
            m.setOriginalTitle(movie.getString(IDENTIFIER_ORIGINAL_TITLE));
            m.setOverview(movie.getString(IDENTIFIER_OVERVIEW));
            m.setVoteAverage(movie.getString(IDENTIFIER_VOTE_AVERAGE));
            m.setReleaseDate(movie.getString(IDENTIFIER_RELEASE_DATE));
            m.setBackdropPath(IDENTIFIER_IMAGE+ movie.getString(IDENTIFIER_BACKDROP_PATH));
            m.setId(movie.getString(IDENTIFIER_ID));
            movies.add(m);

        }

        return movies;
    }
}


