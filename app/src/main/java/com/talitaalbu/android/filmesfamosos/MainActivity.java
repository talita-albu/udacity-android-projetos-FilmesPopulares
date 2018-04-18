package com.talitaalbu.android.filmesfamosos;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.talitaalbu.android.filmesfamosos.model.Movie;
import com.talitaalbu.android.filmesfamosos.servico.MoviesParseJson;
import com.talitaalbu.android.filmesfamosos.servico.Network;
import com.talitaalbu.android.filmesfamosos.utils.MovieAdapter;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String SAVED_LAYOUT_MANAGER = "SavedLayout" ;
    private RecyclerView mRecyclerMovies;
    private MovieAdapter mAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private GridLayoutManager layoutManager;
    private ArrayList<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading);
        mRecyclerMovies = (RecyclerView) findViewById(R.id.recycler_view_movies);
        layoutManager
                = new GridLayoutManager(this, 2);

        mAdapter = new MovieAdapter(this, this);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                int spanCount = layoutManager.getSpanCount();
                return (mAdapter.isLoadMore(position) /* && (position % spanCount == 0) */) ? spanCount : 1;
            }
        });

        mRecyclerMovies.setLayoutManager(layoutManager);
        mRecyclerMovies.setHasFixedSize(false);
        mRecyclerMovies.setAdapter(mAdapter);

        if (savedInstanceState == null || !savedInstanceState.containsKey(SAVED_LAYOUT_MANAGER)) {
            loadMovies(Network.SearchType.POPULAR_MOVIE);
        } else {
            mMovies = savedInstanceState.getParcelableArrayList(SAVED_LAYOUT_MANAGER);
            mAdapter.setData(mMovies);
        }

    }

    private void loadMovies(Network.SearchType type) {
        new GetMoviesTask().execute(type);
    }

    @Override
    public void onClick(Movie selectedMovie) {

        Intent intentToStartDetailActivity = new Intent(this, DetailMovieActivity.class);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_RETURN_RESULT, (Parcelable) selectedMovie);
        startActivity(intentToStartDetailActivity);
    }

    private void showMovies() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerMovies.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerMovies.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ordering,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_top) {
            mAdapter.setData(null);
            loadMovies(Network.SearchType.TOP_RATED_MOVIE);
            return true;
        }
        else if(id== R.id.action_popular)
        {
            mAdapter.setData(null);
            loadMovies(Network.SearchType.POPULAR_MOVIE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVED_LAYOUT_MANAGER, mMovies);
        super.onSaveInstanceState(outState);
    }

    private class GetMoviesTask extends AsyncTask<Network.SearchType, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(Network.SearchType... params) {

            if (params.length == 0) {
                return null;
            }

            Network.SearchType type = params[0];
            URL requestUrl = Network.buildUrl(type);

            try {
                if(isOnline()) {
                    String jsonResponse = Network
                            .getResponseFromHttpUrl(requestUrl);

                    ArrayList<Movie> movies = MoviesParseJson
                            .getMoviesFromJson(jsonResponse);

                    return movies;
                }
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {

                showMovies();
                mMovies = movies;
                mAdapter.setData(movies);
            } else {
                showErrorMessage();
            }
        }
    }

    public void onClickFavorite(View view) {
        Intent intentToStartActivity = new Intent(this, FavoriteActivity.class);
        startActivity(intentToStartActivity);
    }
}
