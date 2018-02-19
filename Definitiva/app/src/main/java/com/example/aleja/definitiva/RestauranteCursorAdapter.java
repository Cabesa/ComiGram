package com.example.aleja.definitiva;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by aleja on 13/02/2018.
 */

public class RestauranteCursorAdapter extends CursorAdapter {

    public RestauranteCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_restaurante, viewGroup, false);
    }
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Referencias UI.
        TextView nameText = (TextView) view.findViewById(R.id.texto_principal);
        RatingBar bar=(RatingBar)view.findViewById(R.id.barraValoracion);


        // Get valores.
        String name = cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.NOMBRE));
        Float valoracion =Float.parseFloat(cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.VALORACION)));
        // Setup.
        nameText.setText(name);
        bar.setRating(valoracion);


    }
}
