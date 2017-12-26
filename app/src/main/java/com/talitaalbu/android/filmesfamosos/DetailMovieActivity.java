package com.talitaalbu.android.filmesfamosos;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.talitaalbu.android.filmesfamosos.model.Movie;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */

public class DetailMovieActivity extends AppCompatActivity {

    TextView mMovieTitle;
    TextView mPlotSynopsis;
    TextView mUserRaiting;
    TextView mReleaseDate;
    ImageView mPostSmallImage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
        mPlotSynopsis = (TextView) findViewById(R.id.tv_plot_synopsis);
        mUserRaiting = (TextView) findViewById(R.id.tv_user_raiting);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mPostSmallImage = (ImageView) findViewById(R.id.iv_poster_small_image);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_RETURN_RESULT)) {
                Movie movie = (Movie) intentThatStartedThisActivity.getSerializableExtra(Intent.EXTRA_RETURN_RESULT);
                populateMovie(movie);
            }
        }
    }

    public void populateMovie(Movie movie)
    {
        if(movie!=null)
        {

        }
    }
}
