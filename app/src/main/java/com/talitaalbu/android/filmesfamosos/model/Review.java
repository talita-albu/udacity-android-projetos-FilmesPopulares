package com.talitaalbu.android.filmesfamosos.model;

import java.io.Serializable;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */

public class Review implements Serializable {

    private String id;
    private String author;
    private String review;
    private String url;

    public Review(){}

    public Review(String id, String author, String review, String url) {
        this.id = id;
        this.author = author;
        this.review = review;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
