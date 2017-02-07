package com.codebreaker.dart.zomato;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.codebreaker.dart.AmazonProductAd;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by abhishek on 2/3/17.
 */

public class ZomatoReq {

    public static final String APIKEY = "2fae47fe77a78e04ba323fe1151537cd";
    private final OkHttpClient client = new OkHttpClient();


    public String makeRequestUrl(Context context){
        GPSTracker gps = new GPSTracker(context);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            return getRequest(latitude, longitude);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        return null;
    }

    private String getRequest(double lat, double lon){
        return "https://api.zomato.com?user-key=" + APIKEY + "&lat=" + lat + "&lon=" + lon;
    }


    private void NetworkUrl(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                Log.d("ZOMATO", res);
            }


        });
    }



}
