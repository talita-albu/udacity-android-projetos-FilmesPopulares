package com.talitaalbu.android.filmesfamosos.servico;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.talitaalbu.android.filmesfamosos.R;
import com.talitaalbu.android.filmesfamosos.data.MoviesDb;
import com.talitaalbu.android.filmesfamosos.data.MoviesProvider;
import com.talitaalbu.android.filmesfamosos.model.Movie;

import java.util.ArrayList;


public class FavoriteTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
    Context context;
    MoviesTaskCompleteListener callback;
    ProgressDialog pDialog;

    private static final String[] FILME_COLUMNS = {
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


    public FavoriteTask(Context ctx, MoviesTaskCompleteListener cb) {
        this.context = ctx;
        this.callback = cb;
        pDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog.setMessage(context.getResources().getString(R.string.loading));
        pDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> results) {
        super.onPostExecute(results);
        if (pDialog != null) {
            pDialog.dismiss();
        }
        callback.onTaskCompleteRequestMovies(results);
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... args) {
        Cursor cursor = context.getContentResolver().query(
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
            return null;
        }
    }
}
