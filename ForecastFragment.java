package com.example.android.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.sunshine.DetailAct;
import com.example.android.sunshine.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class ForecastFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ArrayList<Prediksi_cuaca> cuacas;
    //private OnFragmentInteractionListener mListener;

    public ForecastFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FetchWeatherTask opt = new FetchWeatherTask();
        opt.execute();
        String result=null;
        try {
            result = opt.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(result != null){
            String forecastJSON = result;
            WeatherDataParser wdp = new WeatherDataParser();
            cuacas = new ArrayList<Prediksi_cuaca>();
            try {
                String [] datanya = wdp.getWeatherDataFromJson(forecastJSON, 7);
                for(int i=0; i<datanya.length; i++) {
                    String[] isi = datanya[i].split(" - ");
//                    for (String s : isi) {
//                        Log.d("ForecastFragment", "isi split: " + s);
//                    }
                    String hari, predik,hi, lo;

                    hari = isi[0];
                    predik = isi[1];
                    String [] hilo = isi[2].split("/");
//                    for (String s : hilo) {
//                        Log.d("ForecastFragment", "isi hilo: " + s);
//                    }
                    hi=hilo[0];
                    lo = hilo[1];
                    cuacas.add(new Prediksi_cuaca(hari,predik, Integer.parseInt(hi), Integer.parseInt(lo), R.drawable.sunny));
                }
            }catch (JSONException e){

            }
        }else {
            cuacas.add(new Prediksi_cuaca("Today", "Sunny", 88, 63, R.drawable.sunny));
            cuacas.add(new Prediksi_cuaca("Tomorrow", "Cloudy", 70, 46, R.drawable.cloudy));
            cuacas.add(new Prediksi_cuaca("Monday", "Rainy", 60, 41, R.drawable.rain));
            cuacas.add(new Prediksi_cuaca("Tuesday", "Foggy", 72, 63, R.drawable.foggy));
            cuacas.add(new Prediksi_cuaca("Wednesday", "Rainy", 64, 51, R.drawable.rain));
            cuacas.add(new Prediksi_cuaca("Thursday", "Foggy", 70, 46, R.drawable.foggy));
            cuacas.add(new Prediksi_cuaca("Friday", "Sunny", 76, 68, R.drawable.sunny));
            Log.d("ForecastFragment", "sampe bikin arrayList");
        }


        //mengumpulkan data dummy
//        cuacas.add(new Prediksi_cuaca("Today", "Sunny", 88, 63, R.drawable.sunny));
//        cuacas.add(new Prediksi_cuaca("Tomorrow", "Cloudy", 70, 46, R.drawable.cloudy));
//        cuacas.add(new Prediksi_cuaca("Monday", "Rainy", 60, 41, R.drawable.rain));
//        cuacas.add(new Prediksi_cuaca("Tuesday", "Foggy", 72, 63, R.drawable.foggy));
//        cuacas.add(new Prediksi_cuaca("Wednesday", "Rainy", 64, 51, R.drawable.rain));
//        cuacas.add(new Prediksi_cuaca("Thursday", "Foggy", 70, 46, R.drawable.foggy));
//        cuacas.add(new Prediksi_cuaca("Friday", "Sunny", 76, 68, R.drawable.sunny));
//        Log.d("ForecastFragment", "sampe bikin arrayList");

        list_forecast_adapter itemsAdapter = new list_forecast_adapter(getActivity(), cuacas);
//        Log.d("ForecastFragment", "sampe masukin ke adapter");

        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.list_forecast);
        //Log.d("ForecastFragment", "same dapetin listview");

        listView.setAdapter(itemsAdapter);
        //Log.d("ForecastFragment", "sampe ngeset adapter");

        //supaya bisa diklik
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //String fore = (Prediksi_cuaca) itemsAdapter.getItem(i);
                        //String teks = "list ke-" + String.valueOf(i);
                        //Toast.makeText(getActivity(), teks, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(), DetailAct.class);
                        intent.putExtra("urutan", i);

                        startActivity(intent);
                        //ada hasil yang dapat dikirm ke induk/ context yang memanggil
                        //startActivityForResult(intent, 1);
                    }
                }
        );

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            FetchWeatherTask opt = new FetchWeatherTask();
            opt.execute();
            String result = null;
            try {
                result = opt.get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
//            Toast t = Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT);
//            t.show();
            if(result != null){
                //String forecastJSON = result;
                WeatherDataParser wdp = new WeatherDataParser();
                try {
                    String [] datanya = wdp.getWeatherDataFromJson(result, 7);
                    ArrayList<Prediksi_cuaca> cuacas = new ArrayList<Prediksi_cuaca>();
                    for(int i=0; i<datanya.length; i++) {
                        String[] isi = datanya[i].split(" - ");
                        String hari, predik,hi, lo;

                        hari = isi[0];
                        predik = isi[1];
                        String [] hilo = isi[2].split("/");
                        hi=hilo[0];
                        lo = hilo[1];
                        cuacas.add(new Prediksi_cuaca(hari,predik, Integer.parseInt(hi), Integer.parseInt(lo), R.drawable.sunny));
                    }
                    Fragment frg = null;
                    frg = getFragmentManager().findFragmentByTag("forecastFragment");
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(frg);
                    ft.attach(frg);
                    ft.commit();
                    return true;
                }catch (JSONException e){
                    return true;
                }
            }else return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
