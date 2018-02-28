package com.example.aleja.definitiva;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class RestaurantesAdapter extends ArrayAdapter<Restaurante> {
    public RestaurantesAdapter(Context context, List<Restaurante> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        // ¿Ya se infló este view?
        if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            convertView = inflater.inflate(
                    R.layout.list_item_restaurante,
                    parent,
                    false);

            holder = new ViewHolder();
           ;
            holder.name = (TextView) convertView.findViewById(R.id.texto_principal);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lead actual.
        Restaurante restaurante = getItem(position);

        // Setup.
        holder.name.setText(restaurante.getnombre());


        return convertView;
    }

    static class ViewHolder {

        TextView name;

    }
}