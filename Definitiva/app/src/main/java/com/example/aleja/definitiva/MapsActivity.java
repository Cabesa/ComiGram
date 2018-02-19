package com.example.aleja.definitiva;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RestauranteDbHelper mRestaurantesDbHelper;
    private String categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        categorias=getIntent().getStringExtra("categorias");
        mRestaurantesDbHelper=new RestauranteDbHelper(getApplicationContext());


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        loadRestaurante();

    }

    public void cargarRestaurantes(String categoria,GoogleMap map){


    }
    private void loadRestaurante() {
        new GetRestaurantes().execute();
    }

    private void showRestaurante(Restaurante restaurante) {

        if (categorias.equals("Ver todos")) {
            LatLng rest = new LatLng(Double.parseDouble(restaurante.getlatitud()), Double.parseDouble(restaurante.getlongitud()));
            mMap.addMarker(new MarkerOptions().position(rest).title("Restaurante " + restaurante.getnombre()));
            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(rest)
                    .zoom(11)
                    .build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else if (categorias.equals(restaurante.getcategoria())) {
            LatLng rest = new LatLng(Double.parseDouble(restaurante.getlatitud()), Double.parseDouble(restaurante.getlongitud()));
            mMap.addMarker(new MarkerOptions().position(rest).title("Restaurante " + restaurante.getnombre()));
            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(rest)
                    .zoom(11)
                    .build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else{

        }
    }

    private void showLoadError() {
        Toast.makeText(getApplicationContext(),
                "Error al cargar información", Toast.LENGTH_SHORT).show();
    }

    private class GetRestaurantes extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mRestaurantesDbHelper.getAllRestaurantes();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                showRestaurante(new Restaurante(cursor));
                cursor.moveToNext();
            }
        }

    }

}
