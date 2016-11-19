package com.example.android.sunshine.app;

/**
 * Created by dwina on 11/12/2016.
 */

public class Prediksi_cuaca {
    private String kapan;
    private String forecast;
    private long derajat_tinggi;
    private long derajat_rendah;
    private int ImageResourceId;

    public Prediksi_cuaca(){

    }

    public Prediksi_cuaca(String kpn, String predik, long tinggi, long rendah, int gbr){
        kapan = kpn;
        forecast = predik;
        derajat_rendah = rendah;
        derajat_tinggi = tinggi;
        ImageResourceId = gbr;
    }

    public String getKapan(){
        return kapan;
    }

    public String getForecast(){
        return forecast;
    }

    public long getDerajat_tinggi(){
        return derajat_tinggi;
    }

    public long getDerajat_rendah(){
        return derajat_rendah;
    }

    public int getImageResourceId() {
        return ImageResourceId;
    }
}
