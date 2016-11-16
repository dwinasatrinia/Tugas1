package com.example.android.app;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.android.sunshine.BuildConfig;
import com.example.android.sunshine.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.attr.data;
import static android.R.attr.id;
import static android.R.attr.key;
import static android.content.ContentValues.TAG;
import static android.text.TextUtils.concat;

/**
 * Created by dwina on 11/12/2016.
 * AsyncTask untuk consume REST API dari OpenWeatherMap
 */

public class FetchWeatherTask extends AsyncTask<Void, Void, String> {
    String res = "";
    private static final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    @Override
    protected String doInBackground(Void... urls) {
        //data jason, satuan metric, 1 week forecast
        String header = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&units=metric&cnt=7";
        String keynya ="&APPID="+ BuildConfig.OPEN_WEATHER_MAP_API_KEY; //key username weendut
        String id_city = "&id=1650357"; //city_id = bandung
        //String s = header+id_city+setingLain+keynya;

        HttpURLConnection conn = null;
        BufferedReader reader = null;

        try{
            URL url = new URL(header.concat(id_city).concat(keynya));
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
            res = "";

            while((data=reader.readLine())!=null){
                buffer.append(data+ "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            res = buffer.toString();
            Log.d("FetchWeatherTask","Berhasil, "+res);
        }catch(MalformedURLException e){
            e.printStackTrace();
            res += e.getMessage();
            Log.e("FetchWeatherTask", "Error URL", e);
        }catch(IOException ex){
            Log.e("FetchWeatherTask", "Error IO", ex);
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

//    protected void onPostExecute(String result) {
//        Log.d("Hasil: ", result);
//        Log.d("FetchWeatherTask", "onPostExecute: "+result);
//    }
}
