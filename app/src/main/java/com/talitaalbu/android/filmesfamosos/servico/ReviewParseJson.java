package com.talitaalbu.android.filmesfamosos.servico;

import com.talitaalbu.android.filmesfamosos.model.Review;
import com.talitaalbu.android.filmesfamosos.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */

public final class ReviewParseJson {

    public static ArrayList<Review> getReviewsFromJson(String jsonStr)
            throws JSONException {

        final String IDENTIFIER_ID = "id";
        final String IDENTIFIER_AUTHOR = "author";
        final String IDENTIFIER_REVIEW = "content";
        final String IDENTIFIER_URL = "url";

        ArrayList<Review> reviews;

        JSONObject ReviewJson = new JSONObject(jsonStr);

        JSONArray trailerArray = ReviewJson.getJSONArray("results");

        reviews = new ArrayList<>();

        if (trailerArray.length() == 0) {
            return null;
        }

        for (int i = 0; i < trailerArray.length(); i++) {
            /* Get the JSON object representing the day */
            JSONObject trailer = trailerArray.getJSONObject(i);
            Review r = new Review();
            r.setId(trailer.getString(IDENTIFIER_ID));
            r.setAuthor(trailer.getString(IDENTIFIER_AUTHOR));
            r.setReview(trailer.getString(IDENTIFIER_REVIEW));
            r.setUrl(trailer.getString(IDENTIFIER_URL));
            reviews.add(r);

        }

        return reviews;
    }
}


