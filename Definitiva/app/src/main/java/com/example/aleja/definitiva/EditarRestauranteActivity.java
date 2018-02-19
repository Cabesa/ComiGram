package com.example.aleja.definitiva;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EditarRestauranteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String ARG_RESTAURANTE_ID = "arg_restaurante_id";
    private String mRestauranteId;
    private RestauranteDbHelper mRestaurantesDbHelper;
    private String categorias[];
    TextView txtnombre;
    GoogleMap mMap;
    private double latitud=0;
    private double longitud=0;

    private LocationManager locationManager;
    private LocationListener listener;
    private Location location;
    private Button btnMap;

    RatingBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_restaurante);

        btnMap=(Button)findViewById(R.id.btnMapaEdit);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationManager.removeUpdates(listener);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };


        configure_button();
        bar=(RatingBar)findViewById(R.id.barraValoracionEdit);
        mRestauranteId = getIntent().getStringExtra(RestaurantesActivity.EXTRA_RESTAURANTE_ID);
        txtnombre=(TextView)findViewById(R.id.txtNombreEdit) ;
        categorias = getResources().getStringArray(R.array.categorias);
        Spinner spinner = (Spinner) findViewById(R.id.spinCategoriaEdit);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, categorias);
        spinner.setAdapter(spinnerArrayAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEdit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editRestaurante();
            }
        });
        mRestaurantesDbHelper=new RestauranteDbHelper(getApplicationContext());

        loadRestaurante();

        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapEditar, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
    }
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng rest = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(rest).title(String.valueOf(txtnombre.getText())));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(rest)
                .zoom(11)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }
    private void loadRestaurante() {
        new GetLawyerByIdTask().execute();
    }

    private void showRestaurante(Restaurante restaurante) {

        txtnombre.setText(restaurante.getnombre());
        latitud=Double.parseDouble(restaurante.getlatitud());
        longitud=Double.parseDouble(restaurante.getlongitud());
        bar.setRating(Float.parseFloat(restaurante.getvaloracion()));
    }

    private void showLoadError() {
        Toast.makeText(getApplicationContext(),
                "Error al cargar información", Toast.LENGTH_SHORT).show();
    }

    private class GetLawyerByIdTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mRestaurantesDbHelper.getRestauranteById(mRestauranteId);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                showRestaurante(new Restaurante(cursor));
            } else {
                showLoadError();
            }
        }

    }
    private void editRestaurante() {
        boolean error = false;
        String nombre=txtnombre.getText().toString();
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(getApplicationContext(),
                    "Rellene el campo nombre", Toast.LENGTH_SHORT).show();
            error = true;
        }
        if(error)
        {
            return;
        }
        Restaurante restaurante = new Restaurante(nombre, "", "", "", bar.getRating()+"",latitud+"",longitud+"");

        new EditRestauranteTask().execute(restaurante);

    }
    private class EditRestauranteTask extends AsyncTask<Restaurante, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Restaurante... restaurantes) {
            return mRestaurantesDbHelper.updateRestaurante(restaurantes[0], mRestauranteId) > 0;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            showLawyersScreen(result);
        }

    }
    private void showLawyersScreen(Boolean requery) {
        if (!requery) {
            showAddEditError();
           setResult(Activity.RESULT_CANCELED);
        } else {
            setResult(Activity.RESULT_OK);
        }

       finish();
    }
    private void showAddEditError() {
        Toast.makeText(getApplicationContext(),
                "Error al agregar nueva información", Toast.LENGTH_SHORT).show();
    }
    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // this code won'textView execute IF permissions are not allowed, because in the line above there is return statement.
        btnMap.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {

                //noinspection MissingPermission
                location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                latitud= location.getLatitude();
                longitud=location.getLongitude();
                mMap.clear();
                LatLng rest = new LatLng(latitud, longitud);
                mMap.addMarker(new MarkerOptions().position(rest).title(String.valueOf(txtnombre.getText())));
                CameraPosition cameraPosition = CameraPosition.builder()
                        .target(rest)
                        .zoom(11)
                        .build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });
    }

}





