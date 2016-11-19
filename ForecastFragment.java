package com.example.android.sunshine.app;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.sunshine.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.R.attr.id;
import static android.R.attr.start;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.android.sunshine.R.id.container;


public class ForecastFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ArrayList<Prediksi_cuaca> cuacas;
    list_forecast_adapter itemsAdapter;
    //private OnFragmentInteractionListener mListener;
    private static final String LOG_TAG = ForecastFragment.class.getSimpleName();

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

        cuacas = new ArrayList<Prediksi_cuaca>();

        //FetchWeatherTask opt = new FetchWeatherTask();
        //opt.execute("1642907"); //parameter cityid untuk jakarta
//        String [] result=null;
//        try {
//            result = opt.get();
//            //Log.d(LOG_TAG, result.toString());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        if(result != null){
//            isiArrayList(result);
//        }else {
            //mengumpulkan data dummy
            cuacas.add(new Prediksi_cuaca("Today", "Sunny", 88, 63, R.drawable.sunny));
            cuacas.add(new Prediksi_cuaca("Tomorrow", "Cloudy", 70, 46, R.drawable.cloudy));
            cuacas.add(new Prediksi_cuaca("Monday", "Rainy", 60, 41, R.drawable.rain));
            cuacas.add(new Prediksi_cuaca("Tuesday", "Foggy", 72, 63, R.drawable.foggy));
            cuacas.add(new Prediksi_cuaca("Wednesday", "Rainy", 64, 51, R.drawable.rain));
            cuacas.add(new Prediksi_cuaca("Thursday", "Foggy", 70, 46, R.drawable.foggy));
            cuacas.add(new Prediksi_cuaca("Friday", "Sunny", 76, 68, R.drawable.sunny));
//            Log.d("ForecastFragment", "sampe bikin arrayList");
//        }

        itemsAdapter = new list_forecast_adapter(getActivity(), cuacas);
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
        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_refresh) {
            updateWeather();
//            Toast toast = Toast.makeText(getActivity(), "Seharusnya ud update", Toast.LENGTH_SHORT);
//            toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void isiArrayList(String [] data, String unitType){
        itemsAdapter.clear();
        for(int i=0; i<data.length; i++) {
            String[] isi = data[i].split(" - ");
            String hari, predik,hi, lo;
            double high, low;

            hari = isi[0];
            predik = isi[1];
            String [] hilo = isi[2].split("/");
            hi=hilo[0];
            high = Integer.parseInt(hi);
            lo = hilo[1];
            low = Integer.parseInt(lo);

            if (unitType.equals(getString(R.string.pref_units_imperial))) {
                high = (high * 1.8) + 32;
                low = (low * 1.8) + 32;
            } else if (!unitType.equals(getString(R.string.pref_units_metric))) {
                Log.d(LOG_TAG, "Unit type not found: " + unitType);
            }
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);
//            cuacas.add(new Prediksi_cuaca(hari,predik, Integer.parseInt(hi), Integer.parseInt(lo), R.drawable.sunny));
            itemsAdapter.add(new Prediksi_cuaca(hari,predik, roundedHigh, roundedLow, R.drawable.sunny));
        }

//        itemsAdapter = new list_forecast_adapter(getActivity(), cuacas);
    }

    public void updateWeather(){
        FetchWeatherTask opt = new FetchWeatherTask();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String lokasi = pref.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));

        String unitType = pref.getString( getString(R.string.pref_units_key),
                getString(R.string.pref_units_metric));
        String [] params = {lokasi, unitType};
        opt.execute(params); //parameter cityid default untuk Bandung, unit metric
        String [] result = null;
        try {
            result = opt.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(result != null){
            //cuacas.clear();
            isiArrayList(result, unitType);
            /*Fragment frg = null;
            frg = getFragmentManager().findFragmentByTag("forecastFragment");
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();*/
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }
}
