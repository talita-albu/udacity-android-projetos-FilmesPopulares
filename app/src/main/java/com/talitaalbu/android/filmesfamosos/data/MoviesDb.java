package com.talitaalbu.android.filmesfamosos.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MoviesDb extends SQLiteOpenHelper implements BaseColumns {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MoviesDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Table name
    public static final String TABLE_NAME = "filme";

    public static final String COLUMN_FILME_ID = "filme_id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_IMAGEM_PATH = "imagem_path";
    public static final String COLUMN_CAPA_PATH = "capa_path";
    public static final String COLUMN_SINOPSE = "sinopse";
    public static final String COLUMN_AVALIACAO = "avaliacao";
    public static final String COLUMN_LANCAMENTO = "lancamento";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FILMES_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                this._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_FILME_ID + " TEXT NOT NULL, " +
                COLUMN_TITULO + " TEXT, " +
                COLUMN_IMAGEM_PATH + " TEXT, " +
                COLUMN_CAPA_PATH + " TEXT," +
                COLUMN_SINOPSE + " TEXT, " +
                COLUMN_AVALIACAO + " TEXT, " +
                COLUMN_LANCAMENTO + " TEXT" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_FILMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
