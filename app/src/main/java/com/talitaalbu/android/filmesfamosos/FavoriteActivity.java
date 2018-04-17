package com.talitaalbu.android.filmesfamosos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.talitaalbu.android.filmesfamosos.data.MoviesDb;
import com.talitaalbu.android.filmesfamosos.data.MoviesProvider;
import com.talitaalbu.android.filmesfamosos.model.Movie;
import com.talitaalbu.android.filmesfamosos.servico.MoviesParseJson;
import com.talitaalbu.android.filmesfamosos.servico.MoviesTaskCompleteListener;
import com.talitaalbu.android.filmesfamosos.servico.Network;
import com.talitaalbu.android.filmesfamosos.utils.MovieAdapter;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerMovies;
    private MovieAdapter mAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private TextView mNoFavorite;
    private GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mNoFavorite = (TextView) findViewById(R.id.tv_nomovie_message);
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

        loadMovies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies();
    }

    private void loadMovies() {
        new FavoriteTask().execute();
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

    private void showNoMoviesMessage() {
        mRecyclerMovies.setVisibility(View.INVISIBLE);
        mNoFavorite.setVisibility(View.VISIBLE);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    class FavoriteTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

        private final String[] FILME_COLUMNS = {
                MoviesDb.TABLE_NAME + "." + MoviesDb._ID,
                MoviesDb.COLUMN_FILME_ID,
                MoviesDb.COLUMN_TITULO,
                MoviesDb.COLUMN_IMAGEM_PATH,
                MoviesDb.COLUMN_CAPA_PATH,
                MoviesDb.COLUMN_SINOPSE,
                MoviesDb.COLUMN_AVALIACAO,
                MoviesDb.COLUMN_LANCAMENTO
        };

        public static final int COL_ID = 0;
        static final int COL_FILME_ID = 1;
        static final int COL_TITULO = 2;
        static final int COL_IMAGEM_PATH = 3;
        static final int COL_CAPA_PATH = 4;
        static final int COL_SINOPSE = 5;
        static final int COL_AVALIACAO = 6;
        static final int COL_LANCAMENTO = 7;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null && movies.size()==0)
            {
                showNoMoviesMessage();
            }
            else if (movies!=null)
            {
                showMovies();
                mAdapter.setData(movies);
            } else {
                showErrorMessage();
            }
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... args) {
            if(isOnline()) {
                Cursor cursor = getContentResolver().query(
                        MoviesProvider.CONTENT_URI,
                        FILME_COLUMNS,
                        null,
                        null,
                        null
                );

                if (cursor != null && cursor.moveToFirst()) {
                    ArrayList<Movie> resultFilmes = new ArrayList<Movie>();
                    while (!cursor.isAfterLast()) {
                        String id = cursor.getString(COL_FILME_ID);
                        String lancamento = cursor.getString(COL_LANCAMENTO);
                        String avaliacao = cursor.getString(COL_AVALIACAO);
                        String capa_path = cursor.getString(COL_CAPA_PATH);
                        String imagem_path = cursor.getString(COL_IMAGEM_PATH);
                        String titulo = cursor.getString(COL_TITULO);
                        String sinopse = cursor.getString(COL_SINOPSE);

                        Movie filme = new Movie(id, imagem_path, titulo, sinopse, avaliacao, lancamento, capa_path);
                        resultFilmes.add(filme);

                        cursor.moveToNext();
                    }

                    cursor.close();
                    return resultFilmes;
                } else {
                    ArrayList<Movie> movies = new ArrayList<>();
                    return movies;
                }
            }
            return null;
        }
    }

}
