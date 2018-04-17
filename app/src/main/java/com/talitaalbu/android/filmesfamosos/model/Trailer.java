package com.talitaalbu.android.filmesfamosos.model;

import java.io.Serializable;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */

public class Trailer implements Serializable {

    private String id;
    private String key;
    private String title;
    private String site;
    private String type;

    public Trailer(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
