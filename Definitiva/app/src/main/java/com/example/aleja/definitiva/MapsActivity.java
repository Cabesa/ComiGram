package com.example.aleja.definitiva;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.games.GamesMetadata;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RestauranteDbHelper mRestaurantesDbHelper;
    private String categorias;
    private String categoria[];
    private static int[] numeros={2131230868,2131230810,2131231058,2131231139,2131231059,2131231135,2131230894};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        categoria = getResources().getStringArray(R.array.categorias);
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
        int icono=0;
        LatLng rest= new LatLng(Double.parseDouble(restaurante.getlatitud()), Double.parseDouble(restaurante.getlongitud()));
        for(int i=0;i<categoria.length;i++)
            if  (restaurante.getcategoria().equals(categoria[i])) {
                icono = numeros[i];
                break;
            }


        int height = 100;
        int width = 100;

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(icono);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        if (categorias.equals("Ver todos")||categorias.equals(restaurante.getcategoria())) {
            mMap.addMarker(new MarkerOptions()
                    .position(rest)
                    .title(restaurante.getnombre())
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

        }
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(rest)
                .zoom(11)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
