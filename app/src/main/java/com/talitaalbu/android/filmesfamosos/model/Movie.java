package com.talitaalbu.android.filmesfamosos.model;

import java.io.Serializable;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */

public class Movie implements Serializable {

    private int ID;
    private String posterPath;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
