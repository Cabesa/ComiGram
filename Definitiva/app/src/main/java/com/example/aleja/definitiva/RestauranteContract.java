package com.example.aleja.definitiva;

import android.provider.BaseColumns;

/**
 * Created by aleja on 13/02/2018.
 */

public class RestauranteContract {
    public static abstract class RestauranteEntry implements BaseColumns {
        public static final String TABLE_NAME ="restaurante";
        public static final String ID = "id";
        public static final String NOMBRE = "nombre";
        public static final String CATEGORIA = "categoria";
        public static final String DESCRIPCION = "descripcion";
        public static final String FOTO = "foto";
        public static final String VALORACION = "valoracion";
        public static final String LONGITUD = "longitud";
        public static final String LATITUD = "latitud";

    }
}
