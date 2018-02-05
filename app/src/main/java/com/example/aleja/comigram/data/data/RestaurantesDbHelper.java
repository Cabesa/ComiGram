package com.example.aleja.comigram.data.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.aleja.comigram.data.data.RestauranteContract.RestauranteEntry;

/**
 * Created by aleja on 05/02/2018.
 */

public class RestaurantesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Restaurantes.db";

    public RestaurantesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Comandos SQL
        sqLiteDatabase.execSQL("CREATE TABLE " + RestauranteEntry.NOMBRE_TABLA+ " ("
                + RestauranteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RestauranteEntry.ID + " TEXT NOT NULL,"
                + RestauranteEntry.NOMBRE + " TEXT NOT NULL,"
                + RestauranteEntry.CATEGORIA + " TEXT NOT NULL,"
                + RestauranteEntry.LONGITUD + " TEXT NOT NULL,"
                + RestauranteEntry.LATITUD + " TEXT NOT NULL,"
                + RestauranteEntry.VALORACION + " TEXT NOT NULL,"
                + RestauranteEntry.DESCRIPCION + " TEXT NOT NULL,"
                + RestauranteEntry.FOTO + " TEXT NOT NULL,"
                + "UNIQUE (" + RestauranteEntry.ID + "))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }
}
