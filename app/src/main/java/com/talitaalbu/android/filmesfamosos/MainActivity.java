package com.talitaalbu.android.filmesfamosos;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import java.io.Serializable;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerMovies;
    private MovieAdapter mAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading);
        mRecyclerMovies = (RecyclerView) findViewById(R.id.recycler_view_movies);
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);

        mRecyclerMovies.setLayoutManager(layoutManager);
        mRecyclerMovies.setHasFixedSize(true);
        mAdapter = new MovieAdapter(this, this);
        mRecyclerMovies.setAdapter(mAdapter);

        loadMovies(Network.SearchType.POPULAR_MOVIE);
    }

    private void loadMovies(Network.SearchType type) {
        new GetMoviesTask().execute(type);
    }

    @Override
    public void onClick(Movie selectedMovie) {

        Intent intentToStartDetailActivity = new Intent(this, DetailMovieActivity.class);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_RETURN_RESULT, (Serializable) selectedMovie);
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class GetMoviesTask extends AsyncTask<Network.SearchType, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(Network.SearchType... params) {

            if (params.length == 0) {
                return null;
            }

            Network.SearchType type = params[0];
            URL requestUrl = Network.buildUrl(type);

            try {
                if(isOnline()) {
                    String jsonResponse = Network
                            .getResponseFromHttpUrl(requestUrl);

                    List<Movie> movies = MoviesParseJson
                            .getMoviesFromJson(MainActivity.this, jsonResponse);

                    return movies;
                }
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {

                showMovies();
                mAdapter.setData(movies);
            } else {
                showErrorMessage();
            }
        }
    }
}
