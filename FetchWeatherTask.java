package com.example.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.sunshine.BuildConfig;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dwina on 11/12/2016.
 * AsyncTask untuk consume REST API dari OpenWeatherMap
 */

public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
//    private static final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    @Override
    protected String[] doInBackground(String... params) {
        String format = "json";
//        String units = "metric";

        final String HEADER = "http://api.openweathermap.org/data/2.5/forecast/daily?";
        final String QUERY_PARAM = "id";
        final String FORMAT_PARAM = "mode";
        final String UNITS_PARAM = "units";
        final String DAYS_PARAM = "cnt";
        final String APPID_PARAM = "APPID";
        int numDays = 7;

        String [] res=null;

        if (params.length == 0) {
            return null;
        }
        //data jason, satuan metric, 1 week forecast
//        String header = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&units=metric&cnt=7";

//        String keynya ="&APPID="+ BuildConfig.OPEN_WEATHER_MAP_API_KEY; //key username weendut
//        String id_city = "&id=1650357"; //city_id = bandung
        //String s = header+id_city+setingLain+keynya;

        HttpURLConnection conn = null;
        BufferedReader reader = null;

        try{
            Uri builtUri = Uri.parse(HEADER).buildUpon()
                .appendQueryParameter(QUERY_PARAM, params[0])
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, params[1])
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .build();

            URL url = new URL(builtUri.toString());

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
//            Log.d(LOG_TAG, "sampai konek");
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            if(is == null){
                return null;
            }
            String data = null;

            while((data=reader.readLine())!=null){
                buffer.append(data+ "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            WeatherDataParser wdp = new WeatherDataParser();
            try {
                res = wdp.getWeatherDataFromJson(buffer.toString(), numDays, params[1]);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
//          Log.d("FetchWeatherTask","Berhasil, "+res);
        }catch(MalformedURLException e){
            e.printStackTrace();
            return null;
//            Log.e("FetchWeatherTask", "Error URL", e);
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
//            Log.e("FetchWeatherTask", "Error IO", ex);
        } finally{
            if (conn != null) {
                conn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("FetchWeatherTask", "Error closing stream", e);
                }
            }
        }

        return res;
    }

//    protected void onPostExecute(String [] result) {
//        Log.d("Hasil: ", result);
//        Log.d("FetchWeatherTask", "onPostExecute: "+result);
//    }
}
