package com.example.aleja.definitiva;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RestaurantesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static Spinner spinner;
    private String busqueda[];
    public static final int REQUEST_UPDATE_DELETE_LAWYER = 2;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    public static final String EXTRA_RESTAURANTE_ID = "extra_restaurante_id";
    private ListView mRestaurantesList;
    private RestauranteCursorAdapter mRestauranteAdapter;
    private RestauranteDbHelper mRestauranteDbHelper;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }

    /*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
        }
    */
        busqueda = getResources().getStringArray(R.array.busqueda);
        spinner = (Spinner) findViewById(R.id.spinBusqueda);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, busqueda);
        spinner.setAdapter(spinnerArrayAdapter);
        // Referencias UI
        mRestaurantesList = (ListView) findViewById(R.id.listRestaurantes);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NuevoRestauranteActivity.class);
                startActivityForResult(intent, REQUEST_UPDATE_DELETE_LAWYER);
            }
        });
        mRestaurantesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor currentItem = (Cursor) mRestauranteAdapter.getItem(i);
                String currentLawyerId = currentItem.getString(
                        currentItem.getColumnIndex(RestauranteContract.RestauranteEntry.ID));

                showDetailScreen(currentLawyerId);

            }
        });
        spinner.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        mRestaurantesList.setAdapter(null);
        mRestauranteAdapter = new RestauranteCursorAdapter(getApplicationContext(), null);


        // Setup
        mRestaurantesList.setAdapter(mRestauranteAdapter);

        // Instancia de helper
        mRestauranteDbHelper = new RestauranteDbHelper(getApplicationContext());

        // Carga de datos
        loadRestaurantes();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void showDetailScreen(String restauranteId) {
        Intent intent = new Intent(getApplicationContext(), VerRestauranteActivity.class);
        intent.putExtra(EXTRA_RESTAURANTE_ID, restauranteId);
        startActivityForResult(intent, REQUEST_UPDATE_DELETE_LAWYER);
    }

    public void IrMapa(View v)
    {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("categorias", (String)spinner.getSelectedItem());
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurantes, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        finish();
        startActivity(getIntent());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadRestaurantes() {
        new RestaurantesLoadTask().execute();
    }
    private class RestaurantesLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mRestauranteDbHelper.getAllRestaurantes();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mRestauranteAdapter.swapCursor(cursor);
            } else {
                // Mostrar empty state
            }
        }
    }


}
