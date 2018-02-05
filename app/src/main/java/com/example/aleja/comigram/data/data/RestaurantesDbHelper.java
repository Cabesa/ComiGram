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

        //Insertar datos ficticios para prueba inicial.
        mockData(sqLiteDatabase);
    }
    private void mockData(SQLiteDatabase sqLiteDatabase) {
        mockRestaurante(sqLiteDatabase, new Restaurante("American Restaurant", "Americano", "37.399905", "-5.925305", "10", "Es una pasada, atienden muy bien y la comida muy buena.", ""));

        mockRestaurante(sqLiteDatabase, new Restaurante("La torre de PIZZA", "Italiano", "37.404661", "-5.994504", "7", "He pedido una pizza y estaba muy buena, pero ha tardado un poco.", ""));

        mockRestaurante(sqLiteDatabase, new Restaurante("El kebab de la Meca", "Turco", "37.413740", "-5.965467", "8", "El trato muy bueno y no ha tardado nada en traernos la comida. Lo único malo es el precio, un poco caro pero todo estaba muy bueno.", ""));

        mockRestaurante(sqLiteDatabase, new Restaurante("La lata que cole pol los lincones", "Chino", "37.404510", "-5.973669", "5", "El trato es algo malo, no entienden bien nuestro idioma y algunas veces se equivocan en el pedido. El 5 se lo doy por que es un restaurante bonito y la comida no está mala", ""));

        mockRestaurante(sqLiteDatabase, new Restaurante("OleWacamole", "Mexicano", "37.387287", "-5.959937", "8", "El trato es bueno y la comida bastante buena. No le doy un 10 por que algunas veces llega a tardar hasta 45 minutos en entregar el pedido.", ""));

        mockRestaurante(sqLiteDatabase, new Restaurante("Zampón", "Rápida", "37.385313", "-5.971440", "7", "La comida es muy buena y tarda muy poco en llegar el pedido pero el trato con los trabajadores es un poco pobre.", ""));
    }

    public long mockRestaurante(SQLiteDatabase db, Restaurante Restaurante) {
        return db.insert(
                RestauranteEntry.NOMBRE_TABLA,
                null,
                Restaurante.toContentValues());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }
}
