package com.example.aleja.definitiva;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.File;

public class VerRestauranteActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    private String mRestauranteId;
    public static final String EXTRA_RESTAURANTE_ID = "extra_restaurante_id";
    private TextView mNombre;
    private TextView mCategoria;
    private EditText mDescripcion;
    private String longitud;
    private String latitud;
    private String foto;
    RatingBar bar;
    private LocationListener listener;
    private LocationManager locationManager;

    private ImageView img;
    private RestauranteDbHelper mRestaurantesDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_restaurante);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mNombre=(TextView)findViewById(R.id.txtNombre);
        mCategoria=(TextView)findViewById(R.id.spinCategoria);
        mDescripcion=(EditText)findViewById(R.id.txtDescripcion);
        bar=(RatingBar)findViewById(R.id.barraValoracionVer);
        img=(ImageView)findViewById(R.id.imgVer);
        mRestauranteId = getIntent().getStringExtra(RestaurantesActivity.EXTRA_RESTAURANTE_ID);
        mRestaurantesDbHelper=new RestauranteDbHelper(getApplicationContext());
        loadRestaurante();

        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapView, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
    }
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng rest = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
        mMap.addMarker(new MarkerOptions().position(rest).title("Restaurante " + mNombre.getText()));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(rest)
                .zoom(11)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurante_detail, menu);
        return true;
    }
    private void loadRestaurante() {
        new GetLawyerByIdTask().execute();
    }

    private void showRestaurante(Restaurante restaurante) {

        latitud=restaurante.getlatitud();
        longitud=restaurante.getlongitud();
        mNombre.setText(restaurante.getnombre());
        mCategoria.setText(restaurante.getcategoria());
        mDescripcion.setText(restaurante.getdescripcion());
        bar.setRating(Float.parseFloat(restaurante.getvaloracion()));
        foto=restaurante.getfoto();
        Bitmap icon=null;
        if(foto.equals(""))
        {
            icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.sinimagen);
        }
        else if(foto.startsWith("/"))
        {
            File imgFile = new File(foto);
            if(imgFile.exists()) {
                icon=BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
            else {
                icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.sinimagen);
            }
        }
        else
        {
            icon=StringBitmap.StringToBitMap(foto);
        }
        img.setImageBitmap(icon);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                showEditScreen();
                break;
            case R.id.action_delete:
                /*
		 * Borramos el registro con confirmación
		 */
                AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);

                dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
                dialogEliminar.setTitle(getResources().getString(R.string.restaurente_eliminar_titulo));
                dialogEliminar.setMessage(getResources().getString(R.string.restaurante_eliminar_mensaje));
                dialogEliminar.setCancelable(false);

                dialogEliminar.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int boton) {

				/*
				 * Devolvemos el control
				 *
				 */
                        new DeleteRestauranteTask().execute();
                        setResult(RESULT_OK);
                        finish();
                    }
                });

                dialogEliminar.setNegativeButton(android.R.string.no, null);

                dialogEliminar.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showEditScreen() {
        Intent intent = new Intent(getApplicationContext(), EditarRestauranteActivity.class);
        intent.putExtra(EXTRA_RESTAURANTE_ID, mRestauranteId);
        startActivityForResult(intent, RestaurantesActivity.REQUEST_UPDATE_DELETE_LAWYER);
    }
    private class DeleteRestauranteTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            return mRestaurantesDbHelper.deleteRestaurante(mRestauranteId);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            showRestaurantesScreen(integer > 0);
        }

    }
    private void showRestaurantesScreen(boolean requery) {
        if (!requery) {
            showDeleteError();
        }
        Toast.makeText(getApplicationContext(),R.string.restaurante_eliminar_confirmacion, Toast.LENGTH_SHORT).show();
        finish();
    }
    private void showDeleteError() {
        Toast.makeText(getApplicationContext(),
                "Error al eliminar restaurante", Toast.LENGTH_SHORT).show();
    }


}

