package com.example.aleja.definitiva;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.aleja.definitiva.RestauranteContract.RestauranteEntry;
/**
 * Created by aleja on 13/02/2018.
 */

public class RestauranteDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Restaurantes.db";

    public RestauranteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + RestauranteEntry.TABLE_NAME + " ("
                + RestauranteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RestauranteEntry.ID + " TEXT NOT NULL,"
                + RestauranteEntry.NOMBRE + " TEXT NOT NULL,"
                + RestauranteEntry.CATEGORIA + " TEXT,"
                + RestauranteEntry.DESCRIPCION + " TEXT,"
                + RestauranteEntry.FOTO + " TEXT ,"
                + RestauranteEntry.VALORACION + " TEXT,"
                + RestauranteEntry.LONGITUD + " TEXT,"
                + RestauranteEntry.LATITUD + " TEXT,"
                + "UNIQUE (" + RestauranteEntry.ID + "))");
        // Contenedor de valores

        mockData(db);
    }
    private void mockData(SQLiteDatabase sqLiteDatabase) {


      /*
        mockRestaurante(sqLiteDatabase, new Restaurante("American Restaurant", "Americano", "Es una pasada, atienden muy bien y la comida muy buena.", "", "5", "37.399905", "-5.925305"));
        mockRestaurante(sqLiteDatabase, new Restaurante("La torre de PIZZA", "Italiano", "He pedido una pizza y estaba muy buena, pero ha tardado un poco.", "", "3.5", "37.404661", "-5.994504"));

      mockRestaurante(sqLiteDatabase, new Restaurante("El kebab de la Meca", "Turco", "37.413740", "-5.965467", "8", "El trato muy bueno y no ha tardado nada en traernos la comida. Lo único malo es el precio, un poco caro pero todo estaba muy bueno., ""));

        mockRestaurante(sqLiteDatabase, new Restaurante("La lata que cole pol los lincones", "Chino", "37.404510", "-5.973669", "5", "El trato es algo malo, no entienden bien nuestro idioma y algunas veces se equivocan en el pedido. El 5 se lo doy por que es un restaurante bonito y la comida no está mala", ""));

        mockRestaurante(sqLiteDatabase, new Restaurante("OleWacamole", "Mexicano", "37.387287", "-5.959937", "8", "El trato es bueno y la comida bastante buena. No le doy un 10 por que algunas veces llega a tardar hasta 45 minutos en entregar el pedido.", ""));

        mockRestaurante(sqLiteDatabase, new Restaurante("Zampón", "Española", "37.385313", "-5.971440", "7", "La comida es muy buena y tarda muy poco en llegar el pedido pero el trato con los trabajadores es un poco pobre.", ""));
    */
    }
    public long mockRestaurante(SQLiteDatabase db, Restaurante Restaurante) {
        return db.insert(
                RestauranteEntry.TABLE_NAME,
                null,
                Restaurante.toContentValues());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public long saveRestaurante(Restaurante restaurante) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(
                RestauranteEntry.TABLE_NAME,
                null,
                restaurante.toContentValues());

    }
    public Cursor getAllRestaurantes() {
        if (RestaurantesActivity.spinner.getSelectedItem().equals("Ver todos")) {
            return getReadableDatabase()
                    .query(
                            RestauranteEntry.TABLE_NAME,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);
        }
        else {
            String categoria= (String)RestaurantesActivity.spinner.getSelectedItem();
            return getReadableDatabase().query(
                    RestauranteEntry.TABLE_NAME,
                    null,
                    RestauranteEntry.CATEGORIA + " LIKE ?",
                    new String[]{categoria},
                    null,
                    null,
                    null);
        }

    }


    public Cursor getRestauranteById(String restauranteId) {
            Cursor c = getReadableDatabase().query(
                    RestauranteEntry.TABLE_NAME,
                    null,
                    RestauranteEntry.ID + " LIKE ?",
                    new String[]{restauranteId},
                    null,
                    null,
                    null);
        return c;
    }
    public int deleteRestaurante(String restauranteId) {
        return getWritableDatabase().delete(
                RestauranteEntry.TABLE_NAME,
                RestauranteEntry.ID + " LIKE ?",
                new String[]{restauranteId});
    }
    public int updateRestaurante(Restaurante restaurante, String restauranteId) {
        return getWritableDatabase().update(
                RestauranteEntry.TABLE_NAME,
                restaurante.toContentValues(),
                RestauranteEntry.ID + " LIKE ?",
                new String[]{restauranteId}
        );
    }


}
