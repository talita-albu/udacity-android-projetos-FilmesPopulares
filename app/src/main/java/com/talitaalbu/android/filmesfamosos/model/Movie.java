package com.talitaalbu.android.filmesfamosos.model;

import java.io.Serializable;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */

public class Movie implements Serializable {

    private String posterPath;
    private String backdropPath;
    private String originalTitle;
    private String overview;
    private String voteAverage;
    private String releaseDate;
    private  boolean favored = false;
    private String id;

    public Movie(){}

    public Movie(String id, String backdropPath, String originalTitle, String overview, String voteAverage, String releaseDate, String posterPath) {
        this.id = id;
        this.backdropPath = backdropPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setFavored(boolean favored) {
        this.favored = favored;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
