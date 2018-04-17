package com.talitaalbu.android.filmesfamosos.servico;

import com.talitaalbu.android.filmesfamosos.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */

public final class TrailerParseJson {

    public static ArrayList<Trailer> getTrailersFromJson(String jsonStr)
            throws JSONException {

        final String IDENTIFIER_ID = "id";
        final String IDENTIFIER_KEY = "key";
        final String IDENTIFIER_TITLE = "name";
        final String IDENTIFIER_SITE = "site";
        final String IDENTIFIER_TYPE = "type";

        ArrayList<Trailer> trailers;

        JSONObject TrailerJson = new JSONObject(jsonStr);

        JSONArray trailerArray = TrailerJson.getJSONArray("results");

        trailers = new ArrayList<>();

        if (trailerArray.length() == 0) {
            return null;
        }

        for (int i = 0; i < trailerArray.length(); i++) {
            /* Get the JSON object representing the day */
            JSONObject trailer = trailerArray.getJSONObject(i);
            Trailer t = new Trailer();
            t.setId(trailer.getString(IDENTIFIER_ID));
            t.setKey(trailer.getString(IDENTIFIER_KEY));
            t.setSite(trailer.getString(IDENTIFIER_SITE));
            t.setTitle(trailer.getString(IDENTIFIER_TITLE));
            t.setType(trailer.getString(IDENTIFIER_TYPE));
            trailers.add(t);

        }

        return trailers;
    }
}


