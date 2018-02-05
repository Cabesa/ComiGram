package com.example.aleja.comigram.data.data;

import android.provider.BaseColumns;

/**
 * Esquema base de datos de Restaurantes
 */

public class RestauranteContract {

    public static abstract class RestauranteEntry implements BaseColumns {
        public static final String NOMBRE_TABLA ="restaurante";
        public static final String ID = "id";
        public static final String NOMBRE = "nombre";
        public static final String CATEGORIA = "categoria";
        public static final String VALORACION = "valoracion";
        public static final String FOTO = "foto";
        public static final String DESCRIPCION = "descripcion";
        public static final String LONGITUD = "longitud";
        public static final String LATITUD = "latitud";

    }
}
