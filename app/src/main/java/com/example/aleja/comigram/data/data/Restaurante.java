package com.example.aleja.comigram.data.data;

import android.content.ContentValues;
import java.util.UUID;
import com.example.aleja.comigram.data.data.RestauranteContract.RestauranteEntry;
/**
 Entidad Restaurante
 */

public class Restaurante {
    private String id;
    private String nombre;
    private String categoria;
    private String longitud;
    private String latitud;
    private String valoracion;
    private String descripcion;
    private String foto;

    public Restaurante(String nombre, String categoria, String longitud, String latitud, String valoracion, String descripcion, String foto )
    {
        this.id = UUID.randomUUID().toString();
        this.nombre=nombre;
        this.categoria=categoria;
        this.descripcion=descripcion;
        this.longitud=longitud;
        this.latitud=latitud;
        this.valoracion=valoracion;
        this.foto=foto;
    }
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(RestauranteEntry.ID, id);
        values.put(RestauranteEntry.NOMBRE, nombre);
        values.put(RestauranteEntry.CATEGORIA, categoria);
        values.put(RestauranteEntry.DESCRIPCION, descripcion);
        values.put(RestauranteEntry.LONGITUD, longitud);
        values.put(RestauranteEntry.LATITUD, latitud);
        values.put(RestauranteEntry.VALORACION, valoracion);
        values.put(RestauranteEntry.FOTO, foto);
        return values;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}

