package com.example.android.sunshine.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.R;

import java.util.ArrayList;

/**
 * Created by dwina on 11/12/2016.
 */

public class list_forecast_adapter extends ArrayAdapter<Prediksi_cuaca> {

    private static final String LOG_TAG = list_forecast_adapter.class.getSimpleName();

    public list_forecast_adapter(Context context, ArrayList<Prediksi_cuaca> cuaca){
        super(context, 0, cuaca);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_forecast, parent, false);
        }

        Prediksi_cuaca w1 = getItem(position);
        ImageView iconForecast = (ImageView) listItemView.findViewById(R.id.iconnya);
        TextView kapan = (TextView) listItemView.findViewById(R.id.kapan_TextView);
        TextView penampakan = (TextView) listItemView.findViewById(R.id.penampakan_TextView);
        TextView tinggi = (TextView) listItemView.findViewById(R.id.suhu_rata_TextView);
        TextView rendah = (TextView) listItemView.findViewById(R.id.suhu_rendah_TextView);
//        Log.d("customAdapter", "udah sampe merujuk komponen");
        kapan.setText(w1.getKapan());
        penampakan.setText(w1.getForecast());
        rendah.setText(String.valueOf(w1.getDerajat_rendah()));
        tinggi.setText(String.valueOf(w1.getDerajat_tinggi()));
        iconForecast.setImageResource(w1.getImageResourceId());
//        Log.d("customAdapter", "udah sampe set komponen");

        // Return the whole list item layout (containing 4 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }

}

