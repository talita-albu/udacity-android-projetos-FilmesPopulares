package com.talitaalbu.android.filmesfamosos.servico;

import com.talitaalbu.android.filmesfamosos.model.Movie;

import java.util.ArrayList;



public interface MoviesTaskCompleteListener {
    void onTaskCompleteRequestMovies(ArrayList<Movie> movies);
}