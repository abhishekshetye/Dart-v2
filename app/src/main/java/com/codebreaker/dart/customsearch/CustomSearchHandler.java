package com.codebreaker.dart.customsearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by abhishek on 3/9/17.
 */

public class CustomSearchHandler {

    private final OkHttpClient client = new OkHttpClient();

    CustomListener handler;

    public void setOnCustomHandlerListener(CustomListener handler){
        this.handler = handler;
    }


    public void run(String qry) throws Exception {

        String url = "https://www.googleapis.com/customsearch/v1?key=AIzaSyCDAZMzd_RMQXvQTpq31nJcNqpLPo8Oz10&cx=013036536707430787589:_pqjad5hr1a&q="+ qry + "&alt=json";
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override public void onFailure(Call call, final IOException e) {
                e.printStackTrace();

            }

            @Override public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                        try {

                            String res=response.body().string();
                            JSONObject jObject = new JSONObject(res);
                            JSONArray jArray = jObject.getJSONArray("items");
                            JSONObject oneObject = jArray.getJSONObject(0);
                            String title = oneObject.getString("title");
                            String link = oneObject.getString("link");
                            String snippet = oneObject.getString("snippet");

                            handler.getCustomData(title, link, snippet);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            }


        });

    }
}
