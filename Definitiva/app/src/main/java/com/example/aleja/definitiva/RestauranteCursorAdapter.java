package com.example.aleja.definitiva;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

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
        ImageView img=(ImageView)view.findViewById(R.id.icon);


        // Get valores.
        String name = cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.NOMBRE));
        Float valoracion =Float.parseFloat(cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.VALORACION)));
        String foto= cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.FOTO));

        // Setup.
        nameText.setText(name);
        bar.setRating(valoracion);
        Bitmap icon=null;
        if(foto.equals(""))
        {
            icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.sinimagen);
        }
        else if(foto.startsWith("/"))
        {
            File imgFile = new File(foto);
            if(imgFile.exists()) {
                icon=BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
            else {
                icon = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.sinimagen);
            }
        }
        else
        {
            icon=StringBitmap.StringToBitMap(foto);
        }
        img.setImageBitmap(icon);

    }
}
