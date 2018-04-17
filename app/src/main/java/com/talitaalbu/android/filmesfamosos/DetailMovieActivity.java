package com.talitaalbu.android.filmesfamosos;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
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
import com.talitaalbu.android.filmesfamosos.model.Review;
import com.talitaalbu.android.filmesfamosos.model.Trailer;
import com.talitaalbu.android.filmesfamosos.servico.MoviesParseJson;
import com.talitaalbu.android.filmesfamosos.servico.Network;
import com.talitaalbu.android.filmesfamosos.servico.ReviewParseJson;
import com.talitaalbu.android.filmesfamosos.servico.TrailerParseJson;
import com.talitaalbu.android.filmesfamosos.utils.MovieAdapter;
import com.talitaalbu.android.filmesfamosos.utils.ReviewAdapter;
import com.talitaalbu.android.filmesfamosos.utils.TrailerAdapter;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */

public class DetailMovieActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private ActivityMovieDetailBinding mBinding;
    private Movie movie;
    private TrailerAdapter tAdapter;
    private GridLayoutManager layoutManager;
    private RecyclerView mRecyclerTrailers;

    private ReviewAdapter rAdapter;
    private RecyclerView mRecyclerReviews;
    private GridLayoutManager layoutManagerR;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        setSupportActionBar(mBinding.headerDetail.headerToolbar);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_RETURN_RESULT)) {
                movie = (Movie) intentThatStartedThisActivity.getSerializableExtra(Intent.EXTRA_RETURN_RESULT);
                populateMovie();
            }
        }

        tAdapter = new TrailerAdapter(this, this);
        layoutManager
                = new GridLayoutManager(this, 1);
        mRecyclerTrailers = (RecyclerView) findViewById(R.id.recycler_view_trailers);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                int spanCount = layoutManager.getSpanCount();
                return (tAdapter.isLoadMore(position) /* && (position % spanCount == 0) */) ? spanCount : 1;
            }
        });

        mRecyclerTrailers.setLayoutManager(layoutManager);
        mRecyclerTrailers.setHasFixedSize(false);
        mRecyclerTrailers.setAdapter(tAdapter);
        loadTrailers();

        rAdapter = new ReviewAdapter(this);
        layoutManagerR
                = new GridLayoutManager(this, 1);
        mRecyclerReviews = (RecyclerView) findViewById(R.id.recycler_view_reviews);

        layoutManagerR.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                int spanCount = layoutManagerR.getSpanCount();
                return (rAdapter.isLoadMore(position) /* && (position % spanCount == 0) */) ? spanCount : 1;
            }
        });

        mRecyclerReviews.setLayoutManager(layoutManagerR);
        mRecyclerReviews.setHasFixedSize(false);
        mRecyclerReviews.setAdapter(rAdapter);
        loadReviews();

    }

    private void loadTrailers() {
        new TrailersTask(this).execute(movie.getId());
    }

    private void loadReviews() {
        new ReviewsTask(this).execute(movie.getId());
    }

    private void populateMovie() {
        if (movie != null) {
            Picasso.with(this).load(movie.getBackdropPath()).into(mBinding.headerDetail.coverMovie);
            getSupportActionBar().setTitle(movie.getOriginalTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            mBinding.tvPlotSynopsis.setText(getString(R.string.synopsis_str).toString() + movie.getOverview());
            mBinding.tvUserRating.setText(getString(R.string.rating_str).toString() + movie.getVoteAverage());
            mBinding.tvReleaseDate.setText(getString(R.string.release_date_str).toString() + movie.getReleaseDate());
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

    MenuItem filterMenuItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {

            shareTrailer(R.string.share_template, tAdapter.getItem(0));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void shareTrailer(int messageTemplateResId, Trailer trailer) {
        startActivity(Intent.createChooser(
                createShareIntent(messageTemplateResId, trailer.getTitle(), trailer.getKey()),
                getString(R.string.title_share_trailer)));
    }

    public Intent createShareIntent(int messageTemplateResId, String title, String key) {
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(getString(messageTemplateResId, title, " http://www.youtube.com/watch?v=" + key));
        return builder.getIntent();
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

        if (button.isSelected()) {
            movie.setFavored(false);
            button.setSelected(false);
        } else {
            movie.setFavored(true);
            button.setSelected(true);
        }

        if (!button.isSelected())
            Toast.makeText(this, R.string.message_movie_favored, Toast.LENGTH_SHORT);
    }

    private static boolean checkIfItWasFavorited(Context context, String id) {
        Cursor cursor = context.getContentResolver().query(
                MoviesProvider.CONTENT_URI,
                null, MoviesDb.COLUMN_FILME_ID + " = ?",
                new String[]{id}, null
        );
        int i = cursor.getCount();
        cursor.close();
        if (i == 1)
            return true;
        else
            return false;
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(Trailer selectedTrailer) {

        Trailer trailer = selectedTrailer;
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
        startActivity(intent);
    }


    class TrailersTask extends AsyncTask<String, Void, ArrayList<Trailer>> {
        Context context;
        ProgressDialog pDialog;

        public TrailersTask(Context ctx) {
            this.context = ctx;
            pDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailers) {
            super.onPostExecute(trailers);
            if (trailers != null) {
                tAdapter.setData(trailers);
            } else {
                Toast.makeText(context, context.getString(R.string.no_trailers), Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected ArrayList<Trailer> doInBackground(String... args) {
            if (args.length == 0) {
                return null;
            }

            String movieId = args[0];
            URL requestUrl = Network.buildTrailerUrl(movieId);

            try {
                if (isOnline()) {
                    String jsonResponse = Network
                            .getResponseFromHttpUrl(requestUrl);

                    ArrayList<Trailer> trailers = TrailerParseJson
                            .getTrailersFromJson(jsonResponse);

                    return trailers;
                }
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    class ReviewsTask extends AsyncTask<String, Void, ArrayList<Review>> {
        Context context;
        ProgressDialog pDialog;

        public ReviewsTask(Context ctx) {
            this.context = ctx;
            pDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            super.onPostExecute(reviews);
            if (reviews != null) {
                rAdapter.setData(reviews);
            } else {
                Toast.makeText(context, context.getString(R.string.no_reviews), Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected ArrayList<Review> doInBackground(String... args) {
            if (args.length == 0) {
                return null;
            }

            String movieId = args[0];
            URL requestUrl = Network.buildReviewUrl(movieId);

            try {
                if (isOnline()) {
                    String jsonResponse = Network
                            .getResponseFromHttpUrl(requestUrl);

                    ArrayList<Review> reviews = ReviewParseJson
                            .getReviewsFromJson(jsonResponse);

                    return reviews;
                }
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


}
