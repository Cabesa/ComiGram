package com.example.aleja.definitiva;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;
import com.example.aleja.definitiva.RestauranteContract.RestauranteEntry;

/**
 * Entidad Restaurante
 */

public class Restaurante {
    private String id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private String foto;
    private String valoracion;
    private String latitud;
    private String longitud;

    public Restaurante(String nombre, String categoria, String descripcion, String foto, String valoracion, String latitud, String longitud)
    {
        id = UUID.randomUUID().toString();
        this.nombre=nombre;
        this.categoria=categoria;
        this.descripcion=descripcion;
        this.foto=foto;
        this.valoracion=valoracion;
        this.latitud=latitud;
        this.longitud=longitud;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getnombre() {
        return nombre;
    }

    public void setnombre(String nombre) {
        this.nombre = nombre;
    }

    public String getcategoria() {
        return categoria;
    }

    public void setcategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getdescripcion() {
        return descripcion;
    }

    public void setdescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getfoto() {
        return foto;
    }

    public void setfoto(String foto) {
        this.foto = foto;
    }

    public String getvaloracion() {
        return valoracion;
    }

    public void setvaloracion(String valoracion) {
        this.valoracion = valoracion;
    }

    public String getlatitud() {
        return latitud;
    }

    public void setlatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getlongitud() {
        return longitud;
    }

    public void setlongitud(String longitud) {
        this.longitud = longitud;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(RestauranteEntry.ID, id);
        values.put(RestauranteEntry.NOMBRE, nombre);
        values.put(RestauranteEntry.CATEGORIA, categoria);
        values.put(RestauranteEntry.DESCRIPCION, descripcion);
        values.put(RestauranteEntry.FOTO, foto);
        values.put(RestauranteEntry.VALORACION, valoracion);
        values.put(RestauranteEntry.LONGITUD, longitud);
        values.put(RestauranteEntry.LATITUD, latitud);
        return values;
    }
    public Restaurante(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(RestauranteEntry.ID));
        nombre = cursor.getString(cursor.getColumnIndex(RestauranteEntry.NOMBRE));
        categoria = cursor.getString(cursor.getColumnIndex(RestauranteEntry.CATEGORIA));
        descripcion = cursor.getString(cursor.getColumnIndex(RestauranteEntry.DESCRIPCION));
        foto = cursor.getString(cursor.getColumnIndex(RestauranteEntry.FOTO));
        valoracion = cursor.getString(cursor.getColumnIndex(RestauranteEntry.VALORACION));
        longitud = cursor.getString(cursor.getColumnIndex(RestauranteEntry.LONGITUD));
        latitud = cursor.getString(cursor.getColumnIndex(RestauranteEntry.LATITUD));
    }


    @Override
    public String toString() {
        return "Restaurante{" +
                "ID='" + id + '\'' +
                ", Nombre='" + nombre + '\'' +
                ", Categoria='" + categoria + '\'' +
                ", Descripcion='" + descripcion + '\'' +
                ", Foto='" + foto + '\'' +
                ", Valoracion='" + valoracion + '\'' +
                ", Longitud='" + longitud + '\'' +
                ", Latitud='" + latitud + '\'' +
                '}';
    }
}
