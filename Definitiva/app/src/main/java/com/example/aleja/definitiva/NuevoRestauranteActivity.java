package com.example.aleja.definitiva;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;


public class NuevoRestauranteActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    GoogleMap mMap;

    private double latitud=0;
    private double longitud=0;
    private Button btnFoto;
    private Button btnGaleria;
    private String categorias[];
    TextView txtnombre;
    EditText txtarea;
    private RestauranteDbHelper mRestaurantesDbHelper;
    RatingBar bar;
    final String TAG = "GPS";
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    static String foto="";
    GoogleApiClient gac;
    LocationRequest locationRequest;

    private static int DESDE_CAMARA = 1;
    private static int DESDE_GALERIA = 2;

    private static ImageView imgView;
    private static Spinner spinner;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_restaurante);
        isGooglePlayServicesAvailable();
        btnFoto=(Button)findViewById(R.id.btnFotoNuevo);
        btnGaleria=(Button)findViewById(R.id.btnGaleria);
        txtarea=(EditText)findViewById(R.id.txtDescripcionNuevo);
        if(!isLocationEnabled())
            showAlert();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        gac = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        bar=(RatingBar)findViewById(R.id.barraValoracionNuevo);
        imgView=(ImageView)findViewById(R.id.imgNuevo);
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.sinimagen);
        imgView.setImageBitmap(icon);
        txtnombre = (TextView) findViewById(R.id.txtNombreNuevo);
        categorias = getResources().getStringArray(R.array.categorias);
        spinner = (Spinner) findViewById(R.id.spinCategoriaNuevo);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, categorias);
        spinner.setAdapter(spinnerArrayAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabNuevo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRestaurante();
            }
        });
        mRestaurantesDbHelper = new RestauranteDbHelper(getApplicationContext());

        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapNuevo, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

    }

    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
       mMap = googleMap;


    }

    private void addRestaurante() {
        boolean error = false;
        String nombre = txtnombre.getText().toString();
        String descripcion = txtarea.getText().toString();
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(getApplicationContext(),
                    "Rellene el campo nombre", Toast.LENGTH_SHORT).show();
            error = true;
        }
        if (TextUtils.isEmpty(descripcion)) {
            Toast.makeText(getApplicationContext(),
                    "Rellene el campo descripcion", Toast.LENGTH_SHORT).show();
            error = true;
        }
        if (error) {
            return;
        }
        String categoria=String.valueOf(spinner.getSelectedItem());
        Restaurante restaurante = new Restaurante(nombre, categoria, descripcion, foto, bar.getRating()+"", latitud+"", longitud+"");

        new AddRestauranteTask().execute(restaurante);

    }


    private class AddRestauranteTask extends AsyncTask<Restaurante, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Restaurante... restaurantes) {

            return mRestaurantesDbHelper.saveRestaurante(restaurantes[0]) > 0;


        }

        @Override
        protected void onPostExecute(Boolean result) {
            showLawyersScreen(result);
        }

    }

    private void showLawyersScreen(Boolean requery) {
        if (!requery) {
            showAddEditError();

        }
        finish();
    }

    private void showAddEditError() {
        Toast.makeText(getApplicationContext(),
                "Error al agregar nueva información", Toast.LENGTH_SHORT).show();
    }


    protected void onStart() {
        gac.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        gac.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            updateUI(location);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NuevoRestauranteActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }
        Log.d(TAG, "onConnected");

        Location ll = LocationServices.FusedLocationApi.getLastLocation(gac);
        Log.d(TAG, "LastLocation: " + (ll == null ? "NO LastLocation" : ll.toString()));

        LocationServices.FusedLocationApi.requestLocationUpdates(gac, locationRequest, this);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try{
                        LocationServices.FusedLocationApi.requestLocationUpdates(gac, locationRequest, this);
                    } catch (SecurityException e) {
                        Toast.makeText(NuevoRestauranteActivity.this, "SecurityException:\n" + e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(NuevoRestauranteActivity.this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(NuevoRestauranteActivity.this, "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
        Log.d("DDD", connectionResult.toString());
    }

    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");
        latitud=loc.getLatitude();
        longitud=loc.getLongitude();
        mMap.clear();
        LatLng rest = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(rest).title("Nuevo Restaurante"));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(rest)
                .zoom(11)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean isGooglePlayServicesAvailable() {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        Log.d(TAG, "This device is supported.");
        return true;
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }
    public void fotoNuevo(View v)
    {
        Intent intent = null;
        int code = 0;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        code = DESDE_CAMARA;
        startActivityForResult(intent, code);
    }
    public void galeriaNuevo(View v)
    {
        Intent intent = null;
        int code = 0;
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        code = DESDE_GALERIA;
        startActivityForResult(intent, code);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        boolean errorTomandoFoto = false;
        Bitmap imagen = null;
        try {
            if (requestCode == DESDE_CAMARA) {
                imagen = (Bitmap) data.getParcelableExtra("data");
                foto=StringBitmap.BitMapToString(imagen);

            }

            if (requestCode == DESDE_GALERIA) {
                Uri uri = data.getData();
                imagen = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex); // returns null
                foto=picturePath;
                cursor.close();
            }
        }catch (Exception e) {
            errorTomandoFoto = true;
        }
        if (!errorTomandoFoto) {
            imgView.setImageBitmap(imagen);


        }
    }
}


