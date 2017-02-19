package com.codebreaker.dart.zomato;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by abhishek on 2/3/17.
 */

public class ZomatoHelper {

    public static final String APIKEY = "2fae47fe77a78e04ba323fe1151537cd";
    private final OkHttpClient client = new OkHttpClient();
    private ZomatoListener listener;

    public void setOnZomatoListener(ZomatoListener listener){
        this.listener = listener;
    }

    public String makeLocationBasedRequest(Context context){
        GPSTracker gps = new GPSTracker(context);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            String request =  getRequest(latitude, longitude);
            Log.d("LATLONG", latitude + " " + longitude);
            Log.d("URL", request);
            NetworkUrl(request);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        return null;
    }

    private String getRequest(double lat, double lon){
        return "https://developers.zomato.com/api/v2.1/geocode?"  + "lat=" + lat + "&lon=" + lon;
    }




    private void NetworkUrl(String url){
        Request request = new Request.Builder()
                .addHeader("user-key", APIKEY)
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                Log.d("ZOMATO", res);
                parse(res);
            }

        });
    }

    private void parse(String data){
        List<Restaurant> restuarants = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        try {
            JSONObject jObject = new JSONObject(data);
            JSONArray jArray = jObject.getJSONArray("nearby_restaurants");
            for(int i=0; i<jArray.length() && i<3; i++){

                int max = jArray.length();
                int randomNum = ThreadLocalRandom.current().nextInt(0, max);
                while(visited.contains(randomNum)){
                    randomNum = ThreadLocalRandom.current().nextInt(0, max);
                }
                visited.add(randomNum);

                JSONObject one = jArray.getJSONObject(randomNum);
                JSONObject restaurant = one.getJSONObject("restaurant");
                String name = restaurant.getString("name");
                JSONObject location = restaurant.getJSONObject("location");
                String address = location.getString("address");
                String cusines = restaurant.getString("cuisines");
                JSONObject rating = restaurant.getJSONObject("user_rating");
                String avgrating = rating.getString("aggregate_rating");
                String menuUrl = restaurant.getString("menu_url");
                String imageUrl = restaurant.getString("featured_image");
                Log.d("ZOMATO", name );

                Restaurant res = new Restaurant();
                res.setName(name);
                res.setAddress(address);
                res.setMenuUrl(menuUrl);
                res.setCusines(cusines);
                res.setRating(avgrating);
                res.setImageUrl(imageUrl);
                restuarants.add(res);
            }
            Log.d("ZOMATO", "length" + jArray.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listener.getLocationBasedRestaurants(restuarants);
    }


}
