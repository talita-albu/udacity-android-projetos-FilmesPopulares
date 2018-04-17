package com.talitaalbu.android.filmesfamosos;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.talitaalbu.android.filmesfamosos.data.MoviesDb;
import com.talitaalbu.android.filmesfamosos.data.MoviesProvider;
import com.talitaalbu.android.filmesfamosos.databinding.ActivityMovieDetailBinding;
import com.talitaalbu.android.filmesfamosos.model.Movie;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */

public class DetailMovieActivity extends AppCompatActivity {

//    private TextView mMovieTitle;
//    private TextView mPlotSynopsis;
//    private TextView mUserRating;
//    private TextView mReleaseDate;
//    private ImageView mPostSmallImage;

    private ActivityMovieDetailBinding mBinding;
    private Movie movie;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        setSupportActionBar(mBinding.headerDetail.headerToolbar);


//        mMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
//        mPlotSynopsis = (TextView) findViewById(R.id.tv_plot_synopsis);
//        mUserRating = (TextView) findViewById(R.id.tv_user_rating);
//        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
//        mPostSmallImage = (ImageView) findViewById(R.id.iv_poster_small_image);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_RETURN_RESULT)) {
                movie = (Movie) intentThatStartedThisActivity.getSerializableExtra(Intent.EXTRA_RETURN_RESULT);
                populateMovie();
            }
        }
    }

    private void populateMovie()
    {
        if(movie!=null)
        {
            Picasso.with(this).load(movie.getBackdropPath()).into(mBinding.headerDetail.coverMovie);
            getSupportActionBar().setTitle(movie.getOriginalTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            mBinding.tvPlotSynopsis.setText(getString(R.string.synopsis_str).toString() +movie.getOverview());
            mBinding.tvUserRating.setText(getString(R.string.rating_str).toString()+movie.getVoteAverage());
            mBinding.tvReleaseDate.setText(getString(R.string.release_date_str).toString()+movie.getReleaseDate());
            Picasso.with(this).load(movie.getPosterPath()).into(mBinding.ivPosterSmallImage);

            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    return checkIfItWasFavorited(getBaseContext(), movie.getId());
                }

                @Override
                protected void onPostExecute(Boolean isFavorite) {
                    if (isFavorite) {
                        mBinding.movieFavoriteButton.setSelected(true);
                    } else {
                        mBinding.movieFavoriteButton.setSelected(false);
                    }
                }
            }.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (movie != null) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.details,menu);
            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
//            mAdapter.setData(null);
//            loadMovies(Network.SearchType.TOP_RATED_MOVIE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onClickFavorite(View button) {
        if (movie == null) return;

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return checkIfItWasFavorited(getBaseContext(), movie.getId());
            }

            @Override
            protected void onPostExecute(Boolean isFavorite) {
                if (isFavorite) {

                           getContentResolver().delete(MoviesProvider.CONTENT_URI,
                                   MoviesDb.COLUMN_FILME_ID + " = ?", new String[]{movie.getId()}
                            );

                } else {

                            ContentValues values = new ContentValues();
                            values.put(MoviesDb.COLUMN_FILME_ID, movie.getId());
                            values.put(MoviesDb.COLUMN_TITULO, movie.getOriginalTitle());
                            values.put(MoviesDb.COLUMN_IMAGEM_PATH, movie.getBackdropPath());
                            values.put(MoviesDb.COLUMN_CAPA_PATH, movie.getPosterPath());
                            values.put(MoviesDb.COLUMN_SINOPSE, movie.getOverview());
                            values.put(MoviesDb.COLUMN_AVALIACAO, movie.getVoteAverage());
                            values.put(MoviesDb.COLUMN_LANCAMENTO, movie.getReleaseDate());

                            getContentResolver().insert(MoviesProvider.CONTENT_URI,
                                    values);
                }
            }
        }.execute();

        if(button.isSelected())
        {
            movie.setFavored(false);
            button.setSelected(false);
        }
        else
        {
            movie.setFavored(true);
            button.setSelected(true);
        }

        if (!button.isSelected()) Toast.makeText(this, R.string.message_movie_favored, Toast.LENGTH_SHORT);
    }

    private static boolean checkIfItWasFavorited(Context context, String id) {
        Cursor cursor = context.getContentResolver().query(
                MoviesProvider.CONTENT_URI,
                null, MoviesDb.COLUMN_FILME_ID + " = ?",
                new String[]{id}, null
        );
        int i = cursor.getCount();
        cursor.close();
        if(i==1)
                return true;
        else
            return false;
    }
}
